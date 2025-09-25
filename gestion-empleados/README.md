# Sistema de Gestión de Empleados

Este proyecto es un sistema de gestión de empleados desarrollado con Spring Boot, que permite administrar empleados, departamentos y proyectos.

## Requisitos

- Java 17
- Maven
- Docker y Docker Compose (para ejecutar en contenedores)

## Estructura del Proyecto

El proyecto está organizado en las siguientes capas:

- **Modelo**: Entidades JPA (Empleado, Departamento, Proyecto)
- **Repositorio**: Interfaces para acceso a datos
- **Servicio**: Lógica de negocio
- **Controlador**: API REST

## Ejecución con Docker

### Paso 1: Compilar la aplicación

```bash
mvn clean package -DskipTests
```

### Paso 2: Construir y ejecutar con Docker Compose

```bash
docker-compose up --build
```

La aplicación estará disponible en http://localhost:8080

### Detener los contenedores

```bash
docker-compose down
```

Para eliminar también los volúmenes (datos de la base de datos):

```bash
docker-compose down -v
```

## Configuración

La aplicación utiliza diferentes perfiles:

- **default**: Utiliza H2 (base de datos en memoria) para desarrollo
- **docker**: Utiliza PostgreSQL para ejecución en contenedores

## API REST

### Empleados

- `GET /api/empleados`: Obtener todos los empleados
- `GET /api/empleados/{id}`: Obtener un empleado por ID
- `POST /api/empleados`: Crear un nuevo empleado
- `PUT /api/empleados/{id}`: Actualizar un empleado existente
- `DELETE /api/empleados/{id}`: Eliminar un empleado

### Departamentos

- `GET /api/departamentos`: Obtener todos los departamentos
- `GET /api/departamentos/{id}`: Obtener un departamento por ID
- `POST /api/departamentos`: Crear un nuevo departamento
- `PUT /api/departamentos/{id}`: Actualizar un departamento existente
- `DELETE /api/departamentos/{id}`: Eliminar un departamento

### Proyectos

- `GET /api/proyectos`: Obtener todos los proyectos
- `GET /api/proyectos/{id}`: Obtener un proyecto por ID
- `POST /api/proyectos`: Crear un nuevo proyecto
- `PUT /api/proyectos/{id}`: Actualizar un proyecto existente
- `DELETE /api/proyectos/{id}`: Eliminar un proyecto
- `POST /api/proyectos/{proyectoId}/empleados/{empleadoId}`: Asignar un empleado a un proyecto
- `DELETE /api/proyectos/{proyectoId}/empleados/{empleadoId}`: Desasignar un empleado de un proyecto