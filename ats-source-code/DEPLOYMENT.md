# Deployment Guide for WildFly

This guide explains how to deploy the ATS application to a WildFly server.

## Prerequisites
- WildFly 27+ (Jakarta EE 10 compatible) or WildFly 23+ (Jakarta EE 9 compatible)
- Java 11 or 17
- Maven 3.6+

## Deployment Steps

### 1. Build the WAR File
Run the following command in the project root:
```bash
mvn clean package -DskipTests
```
This will generate `target/ats.war`.

### 2. Deploy to WildFly

#### Option A: Manual Deployment
1. Start your WildFly server.
2. Copy `target/ats.war` to the `standalone/deployments/` directory of your WildFly installation.
3. WildFly will automatically detect and deploy the application.

#### Option B: Via Maven Plugin
If your WildFly server is running and management console is enabled:
```bash
mvn wildfly:deploy
```

#### Option C: Via Management Console
1. Open the WildFly Management Console (usually at `http://localhost:9990`).
2. Navigate to **Deployments**.
3. Click **Add** and upload the `target/ats.war` file.

## Configuration

### Database
The application is configured to use the default `ExampleDS` (H2 database) provided by WildFly. To use a different database:
1. Define a new DataSource in WildFly's `standalone.xml`.
2. Update `src/main/resources/META-INF/persistence.xml` to point to your new JNDI name:
   ```xml
   <jta-data-source>java:jboss/datasources/YourNewDS</jta-data-source>
   ```

### Accessing the App
Once deployed, the application will be available at:
`http://localhost:8080/ats`
