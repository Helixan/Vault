# Vault

## Introduction

Welcome to the Vault Spring application! Vault is a secure and scalable password and document storage solution, providing users with an easy-to-use API to store, retrieve, and manage sensitive information securely. This README covers the server-side setup, configuration, and essential APIs for interacting with the system. Please note that this documentation is focused solely on the server part of the application.

The application utilizes JWT-based authentication, encrypts sensitive data before storing it in MongoDB, and allows users to manage their passwords and documents securely.

## Application Configuration

The `application.properties` file includes important configurations necessary for the Vault Spring application to run effectively. Below, you'll find an overview of the configuration parameters used:

### General Configuration

- **Spring Application Name**
  ```properties
  spring.application.name=Vault
  ```
  This property defines the name of the application as "Vault."

### JWT Configuration

- **JWT Secret Key**
  ```properties
  jwt.secret=YourVerySecretKey12345
  ```
  This is the secret key used to sign and verify JWT tokens. Replace `YourVerySecretKey12345` with a complex and secure key.

- **JWT Expiration Time**
  ```properties
  jwt.expirationMs=86400000
  ```
  The JWT expiration time is set to `86400000` milliseconds, which corresponds to 24 hours.

### Server Port Configuration

- **Server Port**
  ```properties
  server.port=8080
  ```
  This property defines the port on which the server listens. By default, the server runs on port `8080`.

### Logging Configuration

- **Spring Logging Level**
  ```properties
  logging.level.org.springframework=INFO
  ```
  Configures the logging level for Spring framework logs to `INFO`. This setting helps to capture the essential logs without too much verbosity.

### MongoDB Configuration

- **MongoDB Settings**
  ```properties
  spring.data.mongodb.database=vaultdb
  spring.data.mongodb.host=localhost
  spring.data.mongodb.port=27017
  spring.data.mongodb.username=your_username
  spring.data.mongodb.password=your_password
  ```
  These properties are used to configure the connection to the MongoDB database named `vaultdb`. Update the `username` and `password` fields with your MongoDB credentials.

## Security Features

### JWT Authentication

Vault uses JSON Web Tokens (JWT) for user authentication. The `JwtUtils` component is responsible for generating, parsing, and validating JWT tokens:
- **Token Generation**: JWT tokens are generated using a combination of the user's information and a secret key.
- **Validation**: Incoming tokens are validated to ensure their integrity and authenticity before granting access to any API.
- **Token Expiration**: Tokens expire after 24 hours to ensure enhanced security.

### Secure Password Storage

Vault securely stores user passwords by using the **BCryptPasswordEncoder**, which is a strong password hashing algorithm. Passwords are stored in their hashed form, ensuring that even if the database is compromised, the plaintext passwords remain protected.

### Encrypted Document and Password Storage

All sensitive data, such as documents and passwords, are encrypted using the **AES (Advanced Encryption Standard)** algorithm in GCM mode:
- **AES Encryption**: Vault utilizes AES with a key size of 256 bits in GCM mode for encryption. This mode provides confidentiality and data integrity.
- **Unique Key Per User**: Each user has a unique encryption key generated when they register.
- **IV Generation**: A unique Initialization Vector (IV) is generated for each encryption operation, ensuring that the encrypted data remains unpredictable even if the same content is encrypted multiple times.

### Encryption Utility

The encryption and decryption processes are implemented through the `AESUtil` and `EncryptionService` classes, which provide methods to:
- **Generate Encryption Keys**: A new key is generated for each user, enhancing security.
- **Encrypt Sensitive Data**: Sensitive user information such as passwords and documents is encrypted before being stored in MongoDB.
- **Decrypt Sensitive Data**: Data is decrypted only when required, and access to decryption is highly restricted.

## API Overview

The Vault application exposes RESTful APIs for managing users, passwords, and documents. Below is a summary of the key API endpoints.

### Authentication APIs

- **Register User**
    - Endpoint: `/api/auth/register`
    - Method: `POST`
    - Description: Registers a new user with a username, email, and password.

- **Login User**
    - Endpoint: `/api/auth/login`
    - Method: `POST`
    - Description: Authenticates the user and returns a JWT token.

### Password Management APIs

- **Add Password Entry**
    - Endpoint: `/api/passwords`
    - Method: `POST`
    - Description: Adds a new password entry for a user. The password and related notes are securely encrypted before storage.

- **Get All Password Entries**
    - Endpoint: `/api/passwords`
    - Method: `GET`
    - Description: Retrieves all password entries for the authenticated user.

- **Get Password Entry By ID**
    - Endpoint: `/api/passwords/{id}`
    - Method: `GET`
    - Description: Retrieves a specific password entry for the authenticated user.

- **Update Password Entry**
    - Endpoint: `/api/passwords/{id}`
    - Method: `PUT`
    - Description: Updates an existing password entry with new values.

- **Delete Password Entry**
    - Endpoint: `/api/passwords/{id}`
    - Method: `DELETE`
    - Description: Deletes a password entry by ID.

### Document Management APIs

- **Add Document**
    - Endpoint: `/api/documents`
    - Method: `POST`
    - Description: Adds a new document for a user. The document content is encrypted before being stored in the database.

- **Get All Documents**
    - Endpoint: `/api/documents`
    - Method: `GET`
    - Description: Retrieves all documents for the authenticated user.

- **Get Document By ID**
    - Endpoint: `/api/documents/{id}`
    - Method: `GET`
    - Description: Retrieves a specific document for the authenticated user.

- **Update Document**
    - Endpoint: `/api/documents/{id}`
    - Method: `PUT`
    - Description: Updates an existing document with new values.

- **Delete Document**
    - Endpoint: `/api/documents/{id}`
    - Method: `DELETE`
    - Description: Deletes a document by ID.

## How Passwords and Documents Are Stored Securely

1. **Passwords**: User passwords are hashed using `BCryptPasswordEncoder`, ensuring that plaintext passwords are never stored in the database.

2. **Sensitive Data Encryption**: Documents and passwords are encrypted using the AES algorithm before being stored in MongoDB. Each user has a unique key, and different initialization vectors (IVs) are used for each piece of data, ensuring robust security.

3. **Decryption on Demand**: Decryption of encrypted data is performed on demand, using the user-specific encryption key stored in an encoded format. This ensures that the data is readable only by the authenticated user with valid credentials.

## Running the Application

To run the Vault Spring application, ensure that the following prerequisites are met:

### Prerequisites
- **Java 11 or higher**
- **Maven**
- **MongoDB** installed and running on the default port (`27017`). Ensure the database credentials (`username` and `password`) are correctly configured in the `application.properties`.

### Steps to Run
1. **Clone the repository**: Clone the project to your local machine.
   ```bash
   git clone <repository-url>
   ```
2. **Navigate to the project folder**.
   ```bash
   cd vault
   ```
3. **Build the project** using Maven.
   ```bash
   mvn clean install
   ```
4. **Run the application**.
   ```bash
   mvn spring-boot:run
   ```
5. **Access the API** on `http://localhost:8080`.
