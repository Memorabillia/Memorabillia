from flask import Flask, request, render_template
import numpy as np
import pandas as pd
import tensorflow as tf

app = Flask(__name__)

# Load the TFLite model
model_path = 'static/model/my_model.tflite'

with open(model_path, 'rb') as fid:
    tflite_model = fid.read()

interpreter = tf.lite.Interpreter(model_content=tflite_model)
interpreter.allocate_tensors()

input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

# Load the book data
books_df = pd.read_csv('book30k.csv')

def get_top_recommendations(user_id, num_books, interpreter, df, top_n=10):
    user_ids = np.array([[user_id]], dtype=np.float32)
    
    ratings = []
    for isbn_id in range(num_books):
        isbn_ids = np.array([[isbn_id]], dtype=np.float32)
        
        # Prepare input tensor
        interpreter.set_tensor(input_details[0]['index'], user_ids)
        interpreter.set_tensor(input_details[1]['index'], isbn_ids)
        
        # Invoke the interpreter
        interpreter.invoke()
        
        # Get the prediction
        rating = interpreter.get_tensor(output_details[0]['index']).flatten()[0]
        ratings.append(rating)
    
    ratings = np.array(ratings)
    
    # Get the top N recommended book indices
    top_recommendations = np.argsort(ratings)[-top_n:][::-1]
    
    # Get the corresponding ISBNs
    top_isbns = df.iloc[top_recommendations]['ISBN'].values
    
    # Map the ISBNs to unique book titles
    top_titles = df[df['ISBN'].isin(top_isbns)].drop_duplicates(subset=['ISBN'])['Title'].values
    
    return top_titles

@app.route('/')
def home():
    return render_template('index.html')

@app.route('/recommend', methods=['POST'])
def recommend():
    user_id = request.form['user_id']
    
    # Debugging: Print or log the user ID
    print(f"User ID: {user_id}")
    
    # Ensure user ID is in the expected format and range
    if not user_id.isdigit() or len(user_id) > 4:
        return render_template('invalid_user.html'), 400
    
    user_id = int(user_id)
    num_books = len(books_df)
    top_n = 10
    
    # Debugging: Print or log the user ID and book indices
    print(f"User ID: {user_id}, Num Books: {num_books}, Top N: {top_n}")
    
    top_books = get_top_recommendations(user_id, num_books, interpreter, books_df, top_n)
    
    return render_template('result.html', recommendations=top_books)

if __name__ == '__main__':
    app.run(debug=True)
