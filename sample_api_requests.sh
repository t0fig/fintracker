#!/bin/bash

# Create a user
curl -X POST -H "Content-Type: application/json" -d '{
  "username": "john_doe",
  "password": "secure_password",
  "email": "john.doe@example.com"
}' http://localhost:8080/api/v1/user

# Authenticate and get token
token=$(curl -X POST -H "Content-Type: application/json" -d '{
  "username": "john_doe",
  "password": "secure_password"
}' http://localhost:8080/api/v1/user/authenticate | jq -r .jwtToken)

# Add example transactions
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d '{
  "category": "Groceries",
  "amount": 50.00
}' http://localhost:8080/api/v1/transactions

curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d '{
  "category": "Entertainment",
  "amount": 20.00
}' http://localhost:8080/api/v1/transactions

curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d '{
  "category": "Utilities",
  "amount": 100.00
}' http://localhost:8080/api/v1/transactions

curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d '{
  "category": "Groceries",
  "amount": 45.00
}' http://localhost:8080/api/v1/transactions

curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d '{
  "category": "Entertainment",
  "amount": 30.00
}' http://localhost:8080/api/v1/transactions

# Add example income transactions
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d '{
  "category": "income",
  "amount": 5000.00
}' http://localhost:8080/api/v1/transactions

curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d '{
  "category": "income",
  "amount": 1000.00
}' http://localhost:8080/api/v1/transactions

# Get all transactions
curl -H "Authorization: Bearer $token" http://localhost:8080/api/v1/transactions

# Get transactions by categories
curl -H "Authorization: Bearer $token" "http://localhost:8080/api/v1/transactions?category=Groceries,Entertainment"

# Get transactions by start and end date
curl -H "Authorization: Bearer $token" "http://localhost:8080/api/v1/transactions?start=2023-01-01&end=2023-12-31"

# Get transactions by min amount
curl -H "Authorization: Bearer $token" "http://localhost:8080/api/v1/transactions?min=30.00"

# Get transactions by max amount
curl -H "Authorization: Bearer $token" "http://localhost:8080/api/v1/transactions?max=70.00"

# Get transactions by min and max amount
curl -H "Authorization: Bearer $token" "http://localhost:8080/api/v1/transactions?min=30.00&max=70.00"

# Get transactions by categories, date, and amount
curl -H "Authorization: Bearer $token" "http://localhost:8080/api/v1/transactions?category=Groceries,Entertainment&start=2023-01-01&end=2023-12-31&min=30.00&max=70.00"

# Get all income
curl -H "Authorization: Bearer $token" http://localhost:8080/api/v1/income

# Get income by start and end date
curl -H "Authorization: Bearer $token" "http://localhost:8080/api/v1/income?start=2023-01-01&end=2023-12-31"

# Get total spending amount
curl -H "Authorization: Bearer $token" http://localhost:8080/api/v1/spending

# Get spending between start and end dates
curl -H "Authorization: Bearer $token" "http://localhost:8080/api/v1/spending?start=2023-01-01&end=2023-12-31"

# Get spending by categories
curl -H "Authorization: Bearer $token" "http://localhost:8080/api/v1/spending?category=Groceries,Utilities"

# Get spending between start and end dates by categories
curl -H "Authorization: Bearer $token" "http://localhost:8080/api/v1/spending?category=Groceries,Utilities&start=2023-01-01&end=2023-12-31"

# Modify user account (only admin can modify other user account, ordinary users can modify only their own accounts)
curl -X PUT -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d '{
  "username": "new_username",
  "password": "new_password",
  "email": "new.email@example.com"
}' http://localhost:8080/api/v1/user

# Delete User (admin user can delete any user account, while ordinary users can delete only their own account)
curl -X DELETE -H "Authorization: Bearer $token" http://localhost:8080/api/v1/user

# Get Current User
curl -H "Authorization: Bearer $token" http://localhost:8080/api/v1/user
