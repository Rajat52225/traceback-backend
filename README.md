# TraceBack Backend

TraceBack is a cloud-native Lost & Found platform that allows users to report lost or found items, upload images, submit ownership claims with proof, and manage the complete claim-resolution workflow.

This repository contains the Spring Boot backend, Docker configuration, Docker Compose setup, and Kubernetes deployment manifests for TraceBack.

## Live Application

**Frontend:** https://traceback-frontend-wine.vercel.app

**Backend API:** https://traceback-backend-c3ak.onrender.com

> The backend root endpoint may return `403 Forbidden` because TraceBack uses Spring Security and does not expose a public API at `/`.

## Features

- User registration and login
- JWT-based authentication
- BCrypt password hashing
- Stateless Spring Security configuration
- Create Lost and Found item posts
- View active and resolved items
- Upload multiple item images
- AWS S3 image storage
- Submit claims on items
- Upload proof images for claims
- Accept or reject claims
- Automatically mark an item as resolved when a claim is accepted
- Automatically reject remaining pending claims after one claim is accepted
- MongoDB persistent data storage
- REST API architecture
- Docker containerization
- Multi-container deployment using Docker Compose
- Kubernetes deployment
- Kubernetes persistent storage for MongoDB
- Kubernetes Secrets for AWS credentials
- Production deployment using Render and MongoDB Atlas

## Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data MongoDB
- JWT Authentication
- BCrypt
- Maven
- REST APIs

### Database and Storage

- MongoDB
- MongoDB Atlas
- AWS S3

### DevOps and Cloud

- Docker
- Docker Compose
- Kubernetes
- Nginx
- Git
- GitHub
- Render
- Vercel

## Production Architecture

```text
                         Users
                           |
                           v
                  Vercel React Frontend
                           |
                     HTTPS REST API
                           |
                           v
                  Render Spring Backend
                           |
                 +---------+---------+
                 |                   |
                 v                   v
          MongoDB Atlas            AWS S3
          Application Data       Image Storage
```

The React frontend is hosted on Vercel.

The Spring Boot backend is deployed as a Docker container on Render.

MongoDB Atlas provides the production database.

AWS S3 stores item images and claim proof images.

## Kubernetes Architecture

TraceBack was also deployed and tested locally using Docker Desktop Kubernetes.

```text
                         Browser
                            |
                            v
                  Frontend NodePort Service
                            |
                            v
                       Frontend Pod
                      React + Nginx
                            |
                            v
                   Backend NodePort Service
                            |
                            v
                       Backend Pod
                       Spring Boot
                            |
                            v
                  MongoDB ClusterIP Service
                            |
                            v
                       MongoDB Pod
                            |
                            v
                   PersistentVolumeClaim

Backend Pod ------------------------------> AWS S3
```

## Project Structure

```text
traceback-backend/
|
|-- src/
|   `-- main/
|       |-- java/com/traceback/backend/
|       |   |-- config/
|       |   |-- controller/
|       |   |-- dto/
|       |   |-- model/
|       |   |-- repository/
|       |   `-- service/
|       |
|       `-- resources/
|           `-- application.properties
|
|-- k8s/
|   |-- backend-deployment.yaml
|   |-- backend-service.yaml
|   |-- frontend-deployment.yaml
|   |-- frontend-service.yaml
|   |-- mongo-deployment.yaml
|   `-- mongo-service.yaml
|
|-- Dockerfile
|-- docker-compose.yml
|-- pom.xml
`-- README.md
```

## Authentication Flow

```text
User Registration
        |
        v
Password Hashed with BCrypt
        |
        v
User Stored in MongoDB


User Login
        |
        v
Credentials Verified
        |
        v
JWT Generated
        |
        v
JWT Returned to Frontend
        |
        v
JWT Sent in Authorization Header
        |
        v
JwtAuthenticationFilter Validates Token
        |
        v
Protected APIs Become Accessible
```

Protected requests use:

```text
Authorization: Bearer <JWT_TOKEN>
```

## Item Management

Users can create Lost or Found item posts.

Each item contains information such as:

- Title
- Description
- Category
- Item type
- Status
- Location
- Event date
- Owner email
- Image keys
- Image URLs
- Creation timestamp

Items can be browsed through Active and Resolved sections.

## Claim Workflow

```text
User Views Item
       |
       v
Submits Claim
       |
       v
Uploads Proof Images
       |
       v
Item Owner Reviews Claim
       |
       +--------------------+
       |                    |
       v                    v
    ACCEPT                REJECT
       |                    |
       v                    v
Claim Accepted         Claim Rejected
       |
       v
Item Marked Resolved
       |
       v
Remaining Pending Claims Rejected
```

## AWS S3 Integration

AWS S3 is used to store:

- Item images
- Claim proof images

The backend uploads files to S3 and stores the corresponding object keys with application data.

AWS credentials are supplied through environment variables and are never hardcoded into source code.

## Docker

The backend is containerized using Docker.

Build the backend image:

```bash
docker build -t traceback-backend:latest .
```

## Docker Compose

Docker Compose runs the backend and MongoDB together locally.

Start the services:

```bash
docker compose up -d --build
```

Check running containers:

```bash
docker ps
```

Stop the services:

```bash
docker compose down
```

MongoDB data is stored using a named Docker volume.

## Kubernetes Deployment

The Kubernetes manifests are available in the `k8s` directory.

Apply MongoDB resources:

```bash
kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/mongo-service.yaml
```

Create the AWS Kubernetes Secret locally before deploying the backend.

Example:

```bash
kubectl create secret generic traceback-aws-secret \
  --from-literal=AWS_ACCESS_KEY_ID=YOUR_ACCESS_KEY \
  --from-literal=AWS_SECRET_ACCESS_KEY=YOUR_SECRET_KEY
```

Do not commit real credentials or Kubernetes Secret manifests containing credentials.

Deploy the backend:

```bash
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/backend-service.yaml
```

Deploy the frontend:

```bash
kubectl apply -f k8s/frontend-deployment.yaml
kubectl apply -f k8s/frontend-service.yaml
```

Verify the deployment:

```bash
kubectl get pods
kubectl get services
kubectl get deployments
kubectl get pvc
```

## Environment Variables

The backend requires the following environment variables:

```text
MONGODB_URI
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_REGION
```

Example:

```text
MONGODB_URI=mongodb://localhost:27017/traceback_db
AWS_REGION=ap-south-1
```

Never commit real credentials to GitHub.

## Local Development

### Prerequisites

Install:

- Java 17
- Maven
- MongoDB or Docker
- Git

Clone the repository:

```bash
git clone <your-backend-repository-url>
cd traceback-backend
```

Configure the required environment variables and run:

```bash
mvn spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

## Production Deployment

TraceBack uses the following production deployment architecture:

| Component | Platform |
|---|---|
| Frontend | Vercel |
| Backend | Render |
| Database | MongoDB Atlas |
| Image Storage | AWS S3 |

The backend is automatically built from the Dockerfile and deployed on Render.

Production secrets are configured through Render environment variables.

## Security Practices

- Passwords are hashed using BCrypt.
- Authentication uses JWT.
- Spring Security uses stateless sessions.
- Protected APIs require authentication.
- AWS credentials are supplied through environment variables or Kubernetes Secrets.
- Secret files are excluded using `.gitignore`.
- Production credentials are not stored in the repository.
- CORS is restricted to approved frontend origins.

## Challenges Solved

During development and deployment, the following engineering challenges were solved:

- JWT authentication and Spring Security configuration
- Secure password hashing
- MongoDB integration
- AWS S3 image uploads
- Docker image creation
- Multi-container application deployment
- Docker networking and service discovery
- Kubernetes image and deployment issues
- Kubernetes persistent storage
- Kubernetes service networking
- Kubernetes Secrets management
- CORS configuration across multiple environments
- Environment-specific frontend API URLs
- MongoDB Atlas network configuration
- Production MongoDB URI configuration
- Render Docker deployment
- Vercel production deployment

## Future Improvements

- Search and advanced filtering
- Email notifications
- User profiles and dashboards
- Admin dashboard
- Refresh tokens
- Rate limiting
- Automated unit and integration tests
- GitHub Actions CI/CD pipeline
- Kubernetes readiness and liveness probes
- Kubernetes CPU and memory resource limits
- Managed Kubernetes cloud deployment
- Custom domain
- Improved responsive design

## Author

**Rajat Sharma**

B.Tech Computer Science and Engineering

GitHub: https://github.com/Rajat52225
