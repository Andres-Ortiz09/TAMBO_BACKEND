# EJECUCIÓN DEL PROGRAMA TAMBO_BACKEND

Este documento te guía paso a paso para ejecutar el backend del sistema TAMBO, incluyendo imágenes y un video tutorial para descargar e instalar los frameworks necesarios.

## 1. Descarga e Instalación de Frameworks

### Video instalación Java 17
Descarga e instala Java desde la página oficial:

[![Instalar Java](https://www.oracle.com/img/tech/cb88-java-logo-001.jpg)](https://www.youtube.com/watch?v=NDQ9PvjR_Hk&pp=ygUcZGVzY2FyZ2FyIGUgaW5zdGFsYXIgamF2YSAxNw%3D%3D)

[Descargar Java 17](https://www.oracle.com/java/technologies/downloads/)



### Video instalación Maven
Descarga e instala Maven siguiendo el tutorial:

[![Instalar Maven](https://www.campusmvp.es/recursos/recursos/image.axd?picture=/2022/2T/apache-maven.png)](https://www.youtube.com/watch?v=B5dn6DL6_Qk&pp=ygUaZGVzY2FyZ2FyIGUgaW5zdGFsYXIgbWF2ZW4%3D)

[Descargar Maven](https://maven.apache.org/download.cgi)



### Video introducción Spring Boot
Spring Boot se instala automáticamente al compilar el proyecto, pero puedes consultar la documentación oficial:

[![Spring Boot](https://media2.dev.to/dynamic/image/width=1000,height=420,fit=cover,gravity=auto,format=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2Fdmmxiwgyuzodl7yqyuca.jpeg)](https://www.youtube.com/watch?v=DEWrQQu3Vr0&pp=ygUgZGVzY2FyZ2FyIGUgaW5zdGFsYXIgc3ByaW5nIGJvb3Q%3D)

[Documentación Spring Boot](https://spring.io/projects/spring-boot)


### Video instalación IntelliJ IDEA
Puedes usar VS Code o IntelliJ IDEA:

[![Instalar IntelliJ IDEA](https://www.jetbrainsmerchandise.com/media/catalog/product/cache/ecfe99657bcf987295ea6f61f389da7e/s/t/sticker_intellijidea.png)](https://www.youtube.com/watch?v=-QG4TFNKM2w&pp=ygUiZGVzY2FyZ2FyIGUgaW5zdGFsYXIgaW50ZWxsaWogaWRlYQ%3D%3D)

#### Video instalación VS Code
[![Instalar VS Code](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThOU4hIRB20Oy0barVYD6x4N8hQa5QOW7Www&s)](https://www.youtube.com/watch?v=6pD7_rcFrj8&pp=ygUnZGVzY2FyZ2FyIGUgaW5zdGFsYXIgdmlzdWFsIHN0dWRpbyBjb2Rl)


## 2. Clonar el Repositorio

```bash
git clone https://github.com/Andres-Ortiz09/TAMBO_BACKEND.git
cd TAMBO_BACKEND
```

## 3. Compilar el Proyecto

```bash
./mvnw clean install
```

## 4. Ejecutar la Aplicación

```bash
./mvnw spring-boot:run
```
O desde tu IDE ejecuta la clase principal `BackendTamboApplication.java`.

## 5. Acceder a la API

Por defecto, la API estará disponible en:
```
http://localhost:8080
```

