from flask import Flask, request, jsonify
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
books_df = pd.read_csv('supercleaned.csv')

def get_top_recommendations(user_id, num_books, interpreter, df, top_n=10):
    user_ids = np.array([[user_id]], dtype=np.float32)
    
    ratings = []
    for isbn_id in range(num_books):  # Loop through the range of num_books
        isbn_ids = np.array([[isbn_id]], dtype=np.float32)  # Convert isbn_id to ndarray
        
        # Prepare input tensor
        interpreter.set_tensor(input_details[0]['index'], user_ids)  # Set user_ids as input tensor
        interpreter.set_tensor(input_details[1]['index'], isbn_ids)  # Set isbn_ids as input tensor
        
        # Invoke the interpreter
        interpreter.invoke()
        
        # Get the prediction
        rating = interpreter.get_tensor(output_details[0]['index']).flatten()[0]
        ratings.append(rating)
    
    ratings = np.array(ratings)
    
    # Get the top N recommended book indices
    top_recommendations = np.argsort(ratings)[-top_n:][::-1]
    
    # Get the corresponding ISBNs
    top_isbns = df.iloc[top_recommendations]['ISBN'].values.tolist()  # Convert ndarray to list
    
    return top_isbns

@app.route('/recommend', methods=['POST'])
def recommend():
    data = request.json
    
    user_id = data.get('user_id')
    
    if user_id is None or not isinstance(user_id, int) or user_id < 0:
        return jsonify({"error": "Invalid or missing user_id"}), 400

    num_books = len(books_df)
    top_n = 10
    
    top_isbns = get_top_recommendations(user_id, num_books, interpreter, books_df, top_n)
    
    return jsonify({"recommendations": top_isbns})

if __name__ == '__main__':
    app.run(debug=True)
