# Capstone 3 – Spring Boot REST API

This project is a secured RESTful API built with Spring Boot, Spring Security (JWT),
and MySQL. The application supports user authentication, role-based authorization,
product and category management, and a user profile system.
The goal of this capstone was to identify and fix existing bugs,
correctly implement admin-only actions,
and extend the application with a secure Profile feature.


* I fixed search filtering logic, corrected update behavior that caused duplicate products,
* enforced admin-only actions using JWT roles, 
* and implemented a secure Profile feature tied to the authenticated user. 
* I also ensured all endpoints return proper HTTP status codes and follow REST best practices

## Authentication & Security
- JWT-based authentication via `/login`
- Role-based access control
- Admin-only endpoints protected with `@PreAuthorize`
- JWT extracted from `Authorization: Bearer <token>` header


##  Features

### Products
- View all products
- View product by ID
- Search products with filters:
    - Category
    - Minimum price
    - Maximum price
    - Subcategory
- Admin-only create, update, and delete operations

### Categories
- View categories
- Admin-only create, update, and delete operations

### Profiles (Bonus Feature)
- Automatically created when a user registers
- Authenticated users can:
    - View their own profile
    - Update their profile
- Profile access is tied to JWT identity, not user input

## Testing
- Unit tests for DAO search logic
- Controller tests to prevent regression bugs
- Security tests to ensure protected routes require authentication



Bug 1 – Product Search Returned Incorrect Results

Problem: Search filters did not combine properly, leading to incorrect or incomplete results.

Fix Implemented:

Rebuilt SQL query dynamically using conditional clauses

Ensured PreparedStatement parameter order matched query

Validated minPrice/maxPrice logic

Result: Search results now correctly match category, price range, and subCategory combinations.




Bug 2 – Duplicate Products Appearing After Update

Problem: Updating a product was inserting a new row instead of modifying the existing product.

Root Cause: The update logic was incorrectly calling create() instead of update().

Fix Implemented:

Implemented a proper SQL UPDATE statement

Ensured updates use WHERE product_id = ?

Verified ProductController uses update logic only

Result: Products now update correctly without duplication.


Bug 3 – DELETE Category Test Failing

Problem: DELETE requests returned 401 or 400 errors.

Fix Implemented:

Ensured Bearer token was sent correctly

Verified admin role authorization

Corrected request URL formatting

Result: DELETE returns 204 No Content as expected.




* DESIGN & SECURITY DECISIONS

User identity is derived from JWT, not request body

Admin privileges enforced at controller level

SQL injection prevented via PreparedStatements

REST standards followed (204 No Content, proper HTTP codes)

## Status
All required features implemented. Bonus profile feature completed.  
All known bugs fixed and verified.