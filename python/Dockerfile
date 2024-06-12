# Gunakan image dasar yang memiliki Python dan alat untuk TensorFlow Lite
FROM python:3.9-slim

# Atur direktori kerja
WORKDIR /app

# Salin file requirements.txt dan install dependencies
COPY requirements.txt requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Salin semua file ke container
COPY . .

# Ekspose port yang digunakan aplikasi Flask
EXPOSE 8080

# Jalankan aplikasi dengan Gunicorn
CMD ["gunicorn", "-b", ":8080", "app:app"]
