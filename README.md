# 🚀 Sistema de Gestión Empresarial - Spring Boot 3.x

**Sistema completo de gestión de recursos humanos y proyectos** con arquitectura de base de datos mixta (MySQL + MongoDB Atlas), autenticación Spring Security, exportación de datos y UI moderna.


## 📋 Descripción del Proyecto

Aplicación empresarial full-stack que integra:

- **👥 Gestión de Empleados**: CRUD completo almacenado en **MySQL** con interfaz web Thymeleaf
- **📋 Gestión de Proyectos**: CRUD completo almacenado en **MongoDB Atlas** con interfaz web y API REST
- **🔐 Sistema de Autenticación**: Spring Security con login personalizado
- **📊 Exportación de Datos**: Generación de reportes en Excel y PDF
- **💬 Popups Interactivos**: Confirmaciones con SweetAlert2 para mejor UX
- **🎨 UI Moderna**: Diseño responsive con Bootstrap 5.3 y paleta corporativa

---

## 🚨 SOLUCIÓN DE PROBLEMAS COMUNES

### ❌ Error: "Usuario o contraseña incorrectos"

Si actualizaste el sistema desde una versión anterior que usaba `email` en lugar de `username`, necesitas migrar la base de datos:

#### **Solución 1: Migrar la columna existente** (Recomendado)

```sql
-- Ejecutar en MySQL:
USE empresa;
ALTER TABLE usuarios CHANGE COLUMN email username VARCHAR(50) NOT NULL;
```

O ejecutar el script incluido: `migration-email-to-username.sql`

#### **Solución 2: Recrear la tabla de usuarios**

```sql
-- Ejecutar en MySQL (⚠️ ELIMINA TODOS LOS USUARIOS):
USE empresa;
TRUNCATE TABLE usuarios;
```

Después reinicia la aplicación. El sistema creará automáticamente:
- **Usuario:** `admin` / **Contraseña:** `admin123`
- **Usuario:** `supervisor` / **Contraseña:** `super123`

#### **Solución 3: Verificar la estructura de la tabla**

```sql
USE empresa;
DESCRIBE usuarios;
-- La columna debe llamarse 'username', no 'email'
```

---

## 🛠️ Tecnologías y Dependencias

### Backend
- **Spring Boot 3.3.4**
- **Spring Security 6.x** - Autenticación y autorización
- **Spring Data JPA** - Integración con MySQL
- **Spring Data MongoDB** - Integración con MongoDB Atlas
- **Apache POI 5.2.4** - Generación de archivos Excel
- **iText7 7.2.5** - Generación de archivos PDF

### Frontend
- **Thymeleaf 3.x** - Motor de plantillas
- **Bootstrap 5.3** - Framework CSS
- **Font Awesome 6.4** - Iconos
- **SweetAlert2 v11** - Popups interactivos

### Bases de Datos
- **MySQL 8.x** - Base de datos relacional
- **MongoDB Atlas** - Base de datos NoSQL en la nube

### Build Tool
- **Maven 3.9.11**

---

## ⚙️ Configuración Inicial

### Prerrequisitos
- ☕ **Java 17+**
- 🗄️ **MySQL 8.x** (local o remoto)
- 📦 **Maven 3.6+**
- 🌐 **Conexión a Internet** (para MongoDB Atlas)

### 1. Configurar MySQL

```sql
-- Crear base de datos
CREATE DATABASE empresa;

-- Tabla de empleados (se crea automáticamente con JPA)
USE empresa;
```

### 2. Configurar MongoDB Atlas

La aplicación está configurada para usar **MongoDB Atlas** (nube). URI ya configurada:

```
mongodb+srv://fullsena:Sena2025@servidorfull.ig1zknd.mongodb.net/empresa
```

### 3. Archivo `application.properties`

```properties
# ===================================
# MySQL Configuration
# ===================================
spring.datasource.url=jdbc:mysql://localhost:3306/empresa
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# ===================================
# MongoDB Atlas Configuration
# ===================================
spring.data.mongodb.uri=mongodb+srv://fullsena:Sena2025@servidorfull.ig1zknd.mongodb.net/empresa?retryWrites=true&w=majority

# ===================================
# Server Configuration
# ===================================
server.port=8080

# ===================================
# Thymeleaf Configuration
# ===================================
spring.thymeleaf.cache=false
spring.thymeleaf.enabled=true

# ===================================
# Logging
# ===================================
logging.level.org.springframework.data.mongodb.core.MongoTemplate=DEBUG
logging.level.com.empresa.crudmixto=INFO
```

---

## 🚀 Instalación y Ejecución

### Opción 1: Usando Maven Wrapper (Recomendado)

```cmd
cd "c:\JAVA_2931811\Spring Boot Mixed\demo"
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```

### Opción 2: Usando Maven Global

```cmd
mvn clean package
mvn spring-boot:run
```

### Opción 3: Ejecutar JAR

```cmd
.\mvnw.cmd clean package -DskipTests
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

### ✅ Verificar que la aplicación esté corriendo

Verás este mensaje en consola:

```
===============================================
🏢 GESTIÓN EMPRESARIAL - SISTEMA INICIADO 🏢
===============================================
📱 Portal Corporativo: http://localhost:8080/
👥 Recursos Humanos: http://localhost:8080/empleados
📋 Gestión de Proyectos: http://localhost:8080/proyectos
💼 Sistema de gestión integrado empresarial
🔒 Acceso seguro y centralizado a la información
===============================================
```

---

## 🔐 Acceso al Sistema

### Credenciales por Defecto

El sistema crea **2 usuarios** automáticamente en el primer arranque:

#### 👤 Usuario Administrador
```
URL: http://localhost:8080
Usuario: admin
Contraseña: admin123
Rol: ADMIN
```

#### 👤 Usuario Supervisor
```
Usuario: supervisor
Contraseña: super123
Rol: SUPERVISOR
```

**Nota:** Las credenciales se inicializan automáticamente si no existe ningún usuario en la base de datos.

**⚠️ Seguridad:** Cambiar estas contraseñas inmediatamente en producción.

---

## 🎯 Funcionalidades Principales

### 1. 👥 Gestión de Empleados (MySQL)

**Interfaz Web Completa**

- ✅ **CRUD Completo**: Crear, leer, actualizar y eliminar empleados
- ✅ **Búsqueda en Tiempo Real**: Por nombre, cargo o departamento
- ✅ **Validación de Formularios**: Frontend y backend
- ✅ **Vista de Detalle**: Información completa + proyectos asignados
- ✅ **Formato de Salario**: Sin decimales ($50,000)
- ✅ **Popups de Confirmación**: Al actualizar empleados
- ✅ **Exportación**: Excel y PDF con filtros

**Endpoints Web:**
```
GET  /empleados              - Lista de empleados
GET  /empleados/nuevo        - Formulario crear empleado
GET  /empleados/editar/{id}  - Formulario editar empleado
POST /empleados/guardar      - Guardar empleado
GET  /empleados/eliminar/{id}- Eliminar empleado
GET  /empleados/detalle/{id} - Ver empleado y sus proyectos
```

---

### 2. 📋 Gestión de Proyectos (MongoDB)

**Interfaz Web + API REST**

- ✅ **CRUD Completo**: Crear, leer, actualizar y eliminar proyectos
- ✅ **Sistema de Tareas**: Cada proyecto puede tener múltiples tareas
- ✅ **Estados de Tareas**: PENDIENTE, EN_PROGRESO, COMPLETO
- ✅ **Asociación con Empleados**: Cada proyecto tiene un responsable
- ✅ **Búsqueda y Filtros**: Por nombre, descripción o estado
- ✅ **Popups de Confirmación**: Al actualizar proyectos
- ✅ **Exportación**: Excel y PDF con filtros

**Endpoints Web:**
```
GET  /proyectos              - Lista de proyectos
GET  /proyectos/nuevo        - Formulario crear proyecto
GET  /proyectos/editar/{id}  - Formulario editar proyecto
POST /proyectos/guardar      - Guardar proyecto
GET  /proyectos/eliminar/{id}- Eliminar proyecto
GET  /proyectos/detalle/{id} - Ver proyecto con tareas
```

**Endpoints API REST:**
```
GET    /api/proyectos                    - Listar todos los proyectos
POST   /api/proyectos                    - Crear nuevo proyecto
GET    /api/proyectos/{id}               - Obtener proyecto por ID
PUT    /api/proyectos/{id}               - Actualizar proyecto
DELETE /api/proyectos/{id}               - Eliminar proyecto
GET    /api/proyectos/empleado/{empId}  - Proyectos por empleado
POST   /api/proyectos/{id}/tareas       - Agregar tarea
PUT    /api/proyectos/{id}/tareas/{idx} - Actualizar tarea
```

---

### 3. 📊 Sistema de Exportación

**Formatos Disponibles**
- 📄 **Excel (.xlsx)** - Apache POI 5.2.4
- 📄 **PDF (.pdf)** - iText7 7.2.5

**Características**
- ✅ Exportación desde interfaz web con botones
- ✅ Respeta filtros de búsqueda aplicados
- ✅ Formato profesional con colores corporativos
- ✅ Encabezados personalizados
- ✅ Nombre de archivo con timestamp

**Endpoints de Exportación:**
```
GET /api/export/empleados/excel?buscar={filtro}
GET /api/export/empleados/pdf?buscar={filtro}
GET /api/export/proyectos/excel?buscar={filtro}&empleadoId={id}
GET /api/export/proyectos/pdf?buscar={filtro}&empleadoId={id}
```

**Uso desde la interfaz:**
1. Navega a `/empleados` o `/proyectos`
2. Aplica filtros (opcional)
3. Clic en botón **"Exportar"** → Elegir formato
4. El archivo se descarga automáticamente

---

### 4. 🔐 Sistema de Autenticación

**Spring Security 6.x**

- ✅ Login personalizado con diseño moderno
- ✅ Autenticación basada en base de datos
- ✅ Roles: ADMIN, USER
- ✅ Protección de rutas
- ✅ Logout seguro con redirección
- ✅ Mensajes de error y éxito

**Endpoints de Autenticación:**
```
GET  /login               - Página de login
POST /login               - Procesar login
GET  /logout              - Cerrar sesión
GET  /                    - Página de inicio (requiere auth)
```

---

### 5. 💬 Popups Interactivos (SweetAlert2)

**Confirmaciones Inteligentes**

- ✅ **Al Actualizar**: Popup de confirmación "¿Actualizar?"
- ✅ **Al Crear**: No muestra popup (acción directa)
- ✅ **Después de Guardar**: Popup de éxito con auto-cierre
- ✅ **Validación Previa**: Solo muestra popup si formulario es válido
- ✅ **Indicador de Carga**: "Actualizando... Por favor espere"

**Implementado en:**
- Formulario de empleados
- Formulario de proyectos

---

### 6. 🎨 Interfaz de Usuario Moderna

**Diseño Responsive**

- ✅ Bootstrap 5.3
- ✅ Font Awesome 6.4
- ✅ Paleta de colores corporativa (teal)
- ✅ Sticky Footer en todas las páginas
- ✅ Animaciones y transiciones suaves
- ✅ Mensajes de feedback claros
- ✅ Breadcrumbs para navegación

**Paleta de Colores:**
```css
--teal-light: #d1eeea;
--teal-lighter: #a6dcd8;
--teal-medium: #85c3c9;
--teal-accent: #68abb8;
--teal-primary: #4f8fa7;
--teal-dark: #3b738f;
--teal-darker: #295774;
```

---

## 📁 Estructura del Proyecto

```
demo/
├── src/main/
│   ├── java/com/empresa/crudmixto/
│   │   ├── CrudMixtoApplication.java          # Clase principal
│   │   ├── config/
│   │   │   ├── SecurityConfig.java            # Configuración Spring Security
│   │   │   └── DataInitializer.java           # Datos iniciales
│   │   ├── controller/
│   │   │   ├── AuthController.java            # Autenticación
│   │   │   ├── HomeController.java            # Página inicio
│   │   │   ├── EmpleadoController.java        # Web - Empleados
│   │   │   ├── EmpleadoRestController.java    # API REST - Empleados
│   │   │   ├── ProyectoWebController.java     # Web - Proyectos
│   │   │   ├── ProyectoController.java        # API REST - Proyectos
│   │   │   └── ExportController.java          # Exportación Excel/PDF
│   │   ├── entity/
│   │   │   ├── Empleado.java                  # Entidad JPA (MySQL)
│   │   │   ├── Proyecto.java                  # Documento MongoDB
│   │   │   ├── Usuario.java                   # Entidad Usuario (MySQL)
│   │   │   └── Rol.java                       # Enum de roles
│   │   ├── repository/
│   │   │   ├── EmpleadoRepository.java        # Repository JPA
│   │   │   ├── ProyectoRepository.java        # Repository MongoDB
│   │   │   └── UsuarioRepository.java         # Repository Usuarios
│   │   └── service/
│   │       ├── EmpleadoService.java           # Lógica empleados
│   │       ├── ProyectoService.java           # Lógica proyectos
│   │       ├── UsuarioService.java            # Lógica usuarios
│   │       ├── ExportService.java             # Lógica exportación
│   │       └── CustomUserDetailsService.java  # Autenticación
│   └── resources/
│       ├── application.properties             # Configuración
│       └── templates/                         # Plantillas Thymeleaf
│           ├── home.html                      # Página inicio
│           ├── auth/
│           │   └── login.html                 # Página login
│           ├── empleados/
│           │   ├── lista.html                 # Lista empleados
│           │   ├── formulario.html            # Crear/editar empleado
│           │   └── detalle.html               # Detalle empleado
│           └── proyectos/
│               ├── lista.html                 # Lista proyectos
│               ├── formulario.html            # Crear/editar proyecto
│               └── detalle.html               # Detalle proyecto
├── pom.xml                                    # Dependencias Maven
└── README.md                                  # Este archivo
```

---

## 📊 Modelo de Datos

### MySQL - Empleado

```java
@Entity
@Table(name = "empleados")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 100)
    private String cargo;
    
    @Column(nullable = false)
    private Double salario;
    
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    
    @Column(length = 100)
    private String departamento;
}
```

**Tabla generada:**
```sql
CREATE TABLE empleados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cargo VARCHAR(100) NOT NULL,
    salario DOUBLE NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    departamento VARCHAR(100)
);
```

---

### MongoDB - Proyecto

```java
@Document(collection = "proyectos")
public class Proyecto {
    @Id
    private String id;
    
    private String nombre;
    private String descripcion;
    private Long empleadoId;
    private EstadoProyecto estado;
    private List<Tarea> tareas;
    
    public static class Tarea {
        private String descripcion;
        private EstadoTarea estado;
    }
}
```

**Documento MongoDB:**
```json
{
  "_id": "64f1234567890abcdef12345",
  "nombre": "Sistema CRM",
  "descripcion": "Desarrollo de sistema de gestión de clientes",
  "empleadoId": 1,
  "estado": "EN_PROGRESO",
  "tareas": [
    {
      "descripcion": "Diseño de base de datos",
      "estado": "COMPLETO"
    },
    {
      "descripcion": "Desarrollo del backend",
      "estado": "EN_PROGRESO"
    },
    {
      "descripcion": "Interfaz de usuario",
      "estado": "PENDIENTE"
    }
  ]
}
```

**Estados de Proyecto:**
- `ACTIVO` - Proyecto en ejecución
- `EN_PROGRESO` - Proyecto en desarrollo
- `COMPLETADO` - Proyecto finalizado
- `PAUSADO` - Proyecto temporalmente detenido

**Estados de Tarea:**
- `PENDIENTE` - Tarea por iniciar
- `EN_PROGRESO` - Tarea en desarrollo
- `COMPLETO` - Tarea finalizada

---

### MySQL - Usuario (Autenticación)

```java
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Rol rol;
}
```

---

## 📱 Guía de Uso Rápido

### 🔐 Paso 1: Iniciar Sesión

1. Acceder a: http://localhost:8080
2. Ingresar credenciales:
   - **Usuario:** `admin`
   - **Contraseña:** `admin123`
3. Clic en **"Iniciar Sesión"**

---

### 👥 Paso 2: Gestionar Empleados

#### Crear Empleado
1. Ir a **Recursos Humanos** en el navbar
2. Clic en **"Nuevo Empleado"**
3. Llenar formulario:
   - Nombre: Juan Pérez
   - Cargo: Desarrollador Senior
   - Salario: 75000
   - Email: juan.perez@empresa.com
   - Departamento: TI
4. Clic en **"Guardar"**

#### Buscar Empleado
1. En la lista de empleados, usar el campo de búsqueda
2. Buscar por nombre, cargo o departamento
3. Clic en **"Buscar"**

#### Exportar Empleados
1. Aplicar filtros (opcional)
2. Clic en **"Exportar"** → Elegir **Excel** o **PDF**
3. El archivo se descarga automáticamente

#### Editar Empleado
1. Clic en botón **"Editar"** (ícono lápiz)
2. Modificar datos
3. Clic en **"Guardar Cambios"**
4. Aparece popup: **"¿Actualizar Empleado?"**
5. Confirmar → Se muestra popup de éxito

---

### 📋 Paso 3: Gestionar Proyectos

#### Crear Proyecto
1. Ir a **Proyectos** en el navbar
2. Clic en **"Crear Nuevo Proyecto"**
3. Llenar formulario:
   - Nombre: Sistema de Ventas
   - Descripción: Plataforma e-commerce
   - Empleado Responsable: Seleccionar de lista
   - Estado: ACTIVO
4. **Agregar Tareas** (opcional):
   - Descripción: Diseño de base de datos
   - Estado: PENDIENTE
   - Clic en **"+"**
5. Clic en **"Guardar Proyecto"**

#### Editar Proyecto
1. Clic en **"Editar"**
2. Modificar datos o agregar/quitar tareas
3. Clic en **"Guardar Cambios"**
4. Aparece popup: **"¿Actualizar Proyecto?"**
5. Confirmar → Se muestra popup de éxito

#### Ver Detalle de Proyecto
1. Clic en **"Ver Detalle"** (ícono ojo)
2. Ver información completa:
   - Datos del proyecto
   - Empleado responsable
   - Lista de tareas con estados
   - Botones para editar/eliminar

---

### 📊 Paso 4: Exportar Datos

#### Desde Empleados
1. Navegar a `/empleados`
2. Aplicar filtro de búsqueda (opcional)
3. Clic en **"Exportar"** → **"Excel"** o **"PDF"**

#### Desde Proyectos
1. Navegar a `/proyectos`
2. Aplicar filtros (opcional)
3. Clic en **"Exportar"** → **"Excel"** o **"PDF"**

---

### 🚪 Paso 5: Cerrar Sesión

1. Clic en **"Cerrar Sesión"** (navbar superior derecha)
2. Redirige automáticamente al login

---

## 🛡️ Validaciones y Reglas de Negocio

### Empleados
- ✅ **Nombre**: Obligatorio, 1-100 caracteres
- ✅ **Cargo**: Obligatorio, 1-100 caracteres
- ✅ **Salario**: Obligatorio, debe ser mayor a 0
- ✅ **Email**: Obligatorio, formato válido, **único** en el sistema
- ✅ **Departamento**: Opcional, máximo 100 caracteres

### Proyectos
- ✅ **Nombre**: Obligatorio
- ✅ **Descripción**: Obligatoria
- ✅ **Empleado ID**: Obligatorio, **debe existir** en MySQL
- ✅ **Estado**: Obligatorio (ACTIVO, EN_PROGRESO, COMPLETADO, PAUSADO)
- ✅ **Tareas**: Descripción y estado obligatorios

### Seguridad
- ✅ **Autenticación requerida** para todas las rutas (excepto /login)
- ✅ **Sesión persistente** con opción "Recordar mis datos"
- ✅ **Logout seguro** con limpieza de sesión

---

## 🔧 Solución de Problemas

### MySQL no conecta

**Error:** `Connection refused` o `Unknown database 'empresa'`

**Solución:**
```cmd
# 1. Verificar que MySQL esté corriendo
mysql --version

# 2. Crear base de datos
mysql -u root -p
CREATE DATABASE empresa;
exit;

# 3. Verificar credenciales en application.properties
```

---

### MongoDB no conecta

**Error:** `MongoTimeoutException` o `Connection refused`

**Solución:**
- ✅ Verificar conexión a Internet
- ✅ Verificar URI en `application.properties`
- ✅ Comprobar que el cluster de Atlas esté activo
- ✅ Verificar whitelist de IPs en MongoDB Atlas (agregar `0.0.0.0/0` para pruebas)

---

### Puerto 8080 ocupado

**Error:** `Port 8080 is already in use`

**Solución:**
```cmd
# Encontrar proceso usando el puerto
netstat -ano | findstr :8080

# Matar proceso (reemplazar XXXX con el PID)
taskkill /PID XXXX /F

# O cambiar puerto en application.properties
server.port=8081
```

---

### Error al iniciar: "No qualifying bean"

**Solución:**
```cmd
# Limpiar y recompilar
.\mvnw.cmd clean package -DskipTests
.\mvnw.cmd spring-boot:run
```

---

### Problemas con Maven Wrapper

**Error:** `mvnw.cmd` no funciona

**Solución:**
```cmd
# Dar permisos de ejecución
icacls mvnw.cmd /grant Everyone:RX

# O instalar Maven globalmente y usar:
mvn clean install
mvn spring-boot:run
```

---

## 🎨 Características de Diseño

### Sticky Footer
Todas las páginas implementan un **footer pegado al fondo** usando Flexbox CSS, que se posiciona correctamente independientemente de la cantidad de contenido.

```css
body {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

main {
    flex: 1 0 auto;
}

footer {
    flex-shrink: 0;
    margin-top: auto;
}
```

### Popups Interactivos (SweetAlert2)

**Confirmación al Actualizar:**
```javascript
Swal.fire({
    title: '¿Actualizar Empleado?',
    text: "Los cambios se guardaran permanentemente",
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#4f8fa7',
    cancelButtonColor: '#6c757d',
    confirmButtonText: 'Si, actualizar',
    cancelButtonText: 'Cancelar'
});
```

**Éxito tras Guardar:**
```javascript
Swal.fire({
    title: '¡Éxito!',
    text: 'Empleado actualizado correctamente',
    icon: 'success',
    timer: 3000,
    timerProgressBar: true
});
```

---

## 📊 Datos de Prueba

La aplicación se inicializa automáticamente con **12 empleados** y **12 proyectos** de ejemplo en el primer arranque.

### Empleados Iniciales
- Juan Pérez - Desarrollador Senior (TI)
- María García - Gerente de Proyectos (Administración)
- Carlos López - Diseñador UX/UI (Diseño)
- Ana Martínez - Analista de Datos (Análisis)
- _... y 8 más_

### Proyectos Iniciales
- Sistema de Ventas (Juan Pérez)
- Plataforma E-Learning (María García)
- Aplicación Móvil (Carlos López)
- Dashboard Analytics (Ana Martínez)
- _... y 8 más_

Cada proyecto incluye **2-3 tareas** con diferentes estados.

---

## 🚦 Estado del Proyecto

**✅ PROYECTO COMPLETADO Y FUNCIONAL**

### Funcionalidades Implementadas

- [x] **Arquitectura de base de datos mixta** (MySQL + MongoDB)
- [x] **CRUD completo de empleados** (interfaz web)
- [x] **CRUD completo de proyectos** (interfaz web + API REST)
- [x] **Sistema de autenticación** con Spring Security
- [x] **Exportación a Excel y PDF** con filtros
- [x] **Popups de confirmación** con SweetAlert2
- [x] **Diseño responsive** con Bootstrap 5.3
- [x] **Sticky footer** en todas las páginas
- [x] **Validaciones** frontend y backend
- [x] **Búsqueda y filtrado** de datos
- [x] **Datos de prueba** inicializados
- [x] **Manejo de errores** robusto
- [x] **Formato de salarios** sin decimales
- [x] **Navegación integrada** entre módulos
- [x] **Documentación completa**

### Compilación

```
[INFO] BUILD SUCCESS
[INFO] Total time: 6.839 s
✅ Sin errores ni warnings
```

---

## 📚 Ejemplos de Uso de API REST

### 🔧 Colección de Postman Disponible

Para facilitar las pruebas de la API, se incluye una **colección completa de Postman** con todos los endpoints configurados:

- **Archivo:** `Gestion_Empresarial_API.postman_collection.json`
- **Incluye:**
  - ✅ Todos los endpoints de Empleados, Proyectos y Exportación
  - ✅ Configuración de autenticación con Spring Security
  - ✅ Variables de entorno pre-configuradas
  - ✅ Ejemplos de requests completos
  - ✅ Tests automáticos

**Importar en Postman:** File → Import → Seleccionar `Gestion_Empresarial_API.postman_collection.json`

---

### Ejemplos con cURL

### Crear Proyecto

```bash
curl -X POST http://localhost:8080/api/proyectos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Sistema CRM",
    "descripcion": "Desarrollo de sistema de gestión de clientes",
    "empleadoId": 1,
    "estado": "ACTIVO"
  }'
```

### Obtener Todos los Proyectos

```bash
curl -X GET http://localhost:8080/api/proyectos
```

### Obtener Proyecto por ID

```bash
curl -X GET http://localhost:8080/api/proyectos/{id}
```

### Actualizar Proyecto

```bash
curl -X PUT http://localhost:8080/api/proyectos/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Sistema CRM Actualizado",
    "descripcion": "Nueva descripción",
    "empleadoId": 1,
    "estado": "EN_PROGRESO"
  }'
```

### Eliminar Proyecto

```bash
curl -X DELETE http://localhost:8080/api/proyectos/{id}
```

### Agregar Tarea a Proyecto

```bash
curl -X POST http://localhost:8080/api/proyectos/{id}/tareas \
  -H "Content-Type: application/json" \
  -d '{
    "descripcion": "Implementar autenticación",
    "estado": "PENDIENTE"
  }'
```

### Actualizar Estado de Tarea

```bash
curl -X PUT http://localhost:8080/api/proyectos/{id}/tareas/0 \
  -H "Content-Type: application/json" \
  -d '{
    "descripcion": "Implementar autenticación",
    "estado": "COMPLETO"
  }'
```

### Obtener Proyectos por Empleado

```bash
curl -X GET http://localhost:8080/api/proyectos/empleado/{empleadoId}
```

---

## 📖 Recursos Adicionales

### Endpoints Completos

**Autenticación:**
- `GET /login` - Página de login
- `POST /login` - Procesar autenticación
- `GET /logout` - Cerrar sesión

**Empleados (Web):**
- `GET /empleados` - Lista
- `GET /empleados/nuevo` - Formulario crear
- `GET /empleados/editar/{id}` - Formulario editar
- `POST /empleados/guardar` - Guardar
- `GET /empleados/eliminar/{id}` - Eliminar
- `GET /empleados/detalle/{id}` - Ver detalle

**Proyectos (Web):**
- `GET /proyectos` - Lista
- `GET /proyectos/nuevo` - Formulario crear
- `GET /proyectos/editar/{id}` - Formulario editar
- `POST /proyectos/guardar` - Guardar
- `GET /proyectos/eliminar/{id}` - Eliminar
- `GET /proyectos/detalle/{id}` - Ver detalle

**Proyectos (API REST):**
- `GET /api/proyectos` - Listar todos
- `POST /api/proyectos` - Crear
- `GET /api/proyectos/{id}` - Obtener por ID
- `PUT /api/proyectos/{id}` - Actualizar
- `DELETE /api/proyectos/{id}` - Eliminar
- `GET /api/proyectos/empleado/{empId}` - Por empleado
- `POST /api/proyectos/{id}/tareas` - Agregar tarea
- `PUT /api/proyectos/{id}/tareas/{idx}` - Actualizar tarea

**Exportación:**
- `GET /api/export/empleados/excel?buscar={filtro}`
- `GET /api/export/empleados/pdf?buscar={filtro}`
- `GET /api/export/proyectos/excel?buscar={filtro}&empleadoId={id}`
- `GET /api/export/proyectos/pdf?buscar={filtro}&empleadoId={id}`

---

## 🏆 Mejores Prácticas Implementadas

### Arquitectura
- ✅ Separación de responsabilidades (MVC)
- ✅ Servicios para lógica de negocio
- ✅ Repositories para acceso a datos
- ✅ DTOs para transferencia de datos

### Seguridad
- ✅ Autenticación basada en formulario
- ✅ Contraseñas encriptadas con BCrypt
- ✅ Protección CSRF habilitada
- ✅ Roles y permisos configurados

### Base de Datos
- ✅ Uso apropiado de índices
- ✅ Validaciones a nivel de entidad
- ✅ Transacciones manejadas correctamente
- ✅ Conexiones eficientes con pool

### Frontend
- ✅ Diseño responsive (mobile-first)
- ✅ Validación en cliente y servidor
- ✅ Feedback visual inmediato
- ✅ Accesibilidad considerada

### Código
- ✅ Nombres descriptivos
- ✅ Comentarios donde es necesario
- ✅ Manejo de excepciones robusto
- ✅ Logging configurado apropiadamente

---

## 📞 Contacto y Soporte

### En Caso de Problemas

1. **Verificar logs de la aplicación** en consola
2. **Revisar configuración** en `application.properties`
3. **Comprobar conexión** a bases de datos
4. **Validar versiones** de Java y Maven

### Comandos Útiles

```cmd
# Ver logs en tiempo real
.\mvnw.cmd spring-boot:run

# Compilar sin tests
.\mvnw.cmd clean package -DskipTests

# Limpiar todo y recompilar
.\mvnw.cmd clean install

# Ver versión de Java
java -version

# Ver versión de Maven
.\mvnw.cmd -version
```

---

## 📜 Licencia

Este proyecto es para fines educativos y demostrativos.

---

## 🎯 Conclusión

Este proyecto demuestra una implementación completa de:

✅ **Integración de múltiples bases de datos** (MySQL + MongoDB)  
✅ **Arquitectura Spring Boot moderna** con mejores prácticas  
✅ **Seguridad robusta** con Spring Security  
✅ **Interfaz de usuario profesional** y responsive  
✅ **Funcionalidades avanzadas** (exportación, popups, validaciones)  
✅ **Código limpio y mantenible**  

---

## 🚀 Deployment en Heroku

### 🔧 Sistema de Configuración Dual con Spring Profiles

Este proyecto usa **Spring Profiles** para mantener dos configuraciones de base de datos **sin conflictos**:

| Entorno | Base de Datos | Archivo de Configuración | Profile Activo |
|---------|---------------|--------------------------|----------------|
| **🖥️ Local** | MySQL | `application.properties` | (ninguno) |
| **☁️ Heroku** | PostgreSQL | `application-heroku.properties` | `heroku` |

#### ¿Cómo funciona sin conflictos?

**Spring Boot solo activa un profile a la vez:**

- **Sin profile activo** → Usa `application.properties` (MySQL local)
- **Con profile "heroku"** → Usa `application-heroku.properties` (PostgreSQL)

---

### 🔐 Config Vars de Heroku (Variables de Entorno)

Heroku usa **Config Vars** para almacenar información sensible fuera del código fuente. Esto mejora la seguridad y facilita el manejo de diferentes entornos.

#### ¿Qué son las Config Vars?

Son **variables de entorno** que Heroku inyecta en tu aplicación en tiempo de ejecución. Spring Boot las lee usando la sintaxis `${VARIABLE_NAME:valor_por_defecto}`.

#### Config Vars Requeridas

| Variable | Descripción | ¿Se crea automáticamente? |
|----------|-------------|---------------------------|
| `DATABASE_URL` | URL completa de PostgreSQL | ✅ **SÍ** (al agregar addon PostgreSQL) |
| `MONGODB_URI` | URI de MongoDB Atlas | ❌ NO (debes configurarla manualmente) |
| `PORT` | Puerto HTTP | ✅ **SÍ** (Heroku lo asigna dinámicamente) |

**🎯 Solo necesitas configurar manualmente:** `MONGODB_URI`

#### ¿Cómo Spring Boot Lee las Config Vars?

En `application-heroku.properties`:

```properties
# PostgreSQL: Heroku inyecta DATABASE_URL automáticamente
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/empresa}

# MongoDB: Debes configurar MONGODB_URI manualmente
spring.data.mongodb.uri=${MONGODB_URI:mongodb+srv://localhost/empresa}
```

**Flujo de resolución:**
1. Spring Boot busca la variable `DATABASE_URL` en las Config Vars de Heroku
2. Si existe (✅ creada automáticamente), la usa
3. Si NO existe, usa el valor después de `:` (para desarrollo local)

#### Ventajas de Config Vars

✅ **Seguridad**: Credenciales fuera del código  
✅ **Flexibilidad**: Cambia configuración sin re-deployar  
✅ **Múltiples entornos**: Diferentes valores en staging/producción  
✅ **No comiteas secretos**: GitHub no tiene credenciales reales  

#### Comandos Útiles

```cmd
# Ver todas las Config Vars
heroku config

# Agregar/actualizar una variable
heroku config:set POSTGRES_USERNAME=nuevo_usuario

# Eliminar una variable
heroku config:unset POSTGRES_USERNAME

# Ver logs para debug
heroku logs --tail
```

---

#### 1. Instalar Heroku CLI
```cmd
# Descargar desde: https://devcenter.heroku.com/articles/heroku-cli
```

#### 2. Login y Crear App
```cmd
heroku login
heroku create gestion-empresarial-2025
```

#### 3. Agregar PostgreSQL
```cmd
heroku addons:create heroku-postgresql:essential-0
```

#### 4. Configurar Variables de Entorno (Config Vars)

Heroku utiliza **Config Vars** para almacenar información sensible de forma segura.

**✅ Variables Automáticas (No requieren configuración):**
- `DATABASE_URL` - Creada automáticamente al agregar el addon PostgreSQL
- `PORT` - Asignada dinámicamente por Heroku

**⚙️ Variables Manuales (Debes configurar):**
Solo necesitas configurar `MONGODB_URI` para MongoDB Atlas.

**Opción A: Desde Heroku Dashboard (Web)**
1. Ve a tu app en https://dashboard.heroku.com
2. Pestaña **Settings** → **Config Vars** → **Reveal Config Vars**
3. Agrega solo esta variable:

| KEY | VALUE |
|-----|-------|
| `MONGODB_URI` | `mongodb+srv://fullsena:Sena2025@servidorfull.ig1zknd.mongodb.net/empresa?retryWrites=true&w=majority&appName=SERVIDORFULL` |

**Opción B: Desde Heroku CLI**
```cmd
# Solo necesitas configurar MongoDB Atlas
heroku config:set MONGODB_URI=mongodb+srv://fullsena:Sena2025@servidorfull.ig1zknd.mongodb.net/empresa?retryWrites=true&w=majority&appName=SERVIDORFULL

# Ver todas las variables configuradas
heroku config
```

**📝 Nota Importante:**
- ✅ `DATABASE_URL` se crea **automáticamente** al agregar el addon PostgreSQL
- ✅ Heroku **rota credenciales periódicamente** y actualiza `DATABASE_URL` automáticamente
- ✅ Solo necesitas configurar manualmente `MONGODB_URI`
- ✅ **NO** subas credenciales de MongoDB a GitHub (usa Config Vars)

#### 5. Deploy
```cmd
git init
git add .
git commit -m "Initial deployment"
git push heroku main
```

#### 6. Abrir Aplicación
```cmd
heroku open
```

---

**🎉 ¡Proyecto listo para usar!**

Accede a http://localhost:8080 con las credenciales `admin` / `admin123` y comienza a gestionar empleados y proyectos.

---

**Última actualización:** 15 de octubre de 2025  
**Versión:** 0.0.1-SNAPSHOT  
**Estado:** ✅ PRODUCCIÓN (Local + Heroku Ready)
