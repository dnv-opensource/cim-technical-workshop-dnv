FROM maven:3.9-eclipse-temurin-17

WORKDIR /workspace

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy the rest of the project
COPY . .

# Install useful tools
RUN apt-get update && apt-get install -y \
    vim \
    nano \
    curl \
    && rm -rf /var/lib/apt/lists/*

CMD ["bash"]
