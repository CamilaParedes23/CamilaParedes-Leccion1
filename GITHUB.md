# Guía para Subir el Proyecto a GitHub

## 🎯 Objetivo
Subir el proyecto completo al repositorio:  
**https://github.com/CamilaParedes23/CamilaParedes-Leccion1.git**

---

## 📋 Paso 1: Preparación Inicial

### Verificar que Git esté instalado
```powershell
git --version
```

Si no está instalado, descárgalo de: https://git-scm.com/

### Configurar Git (si es primera vez)
```powershell
git config --global user.name "Camila Paredes"
git config --global user.email "tu.email@ejemplo.com"
```

---

## 📂 Paso 2: Inicializar Repositorio Local

```powershell
# Navegar a la carpeta del proyecto
cd c:\Users\usuario\Documents\UNIVERSIDAD\7MO\DISTRIBUIDAS\PRIMERO\PRUEBA

# Inicializar repositorio git
git init

# Verificar estado
git status
```

**Salida esperada:** Lista de archivos sin seguimiento (untracked)

---

## 🔗 Paso 3: Conectar con GitHub

```powershell
# Agregar el repositorio remoto
git remote add origin https://github.com/CamilaParedes23/CamilaParedes-Leccion1.git

# Verificar que se agregó correctamente
git remote -v
```

**Salida esperada:**
```
origin  https://github.com/CamilaParedes23/CamilaParedes-Leccion1.git (fetch)
origin  https://github.com/CamilaParedes23/CamilaParedes-Leccion1.git (push)
```

---

## ➕ Paso 4: Añadir Archivos

```powershell
# Ver qué archivos se van a añadir
git status

# Añadir todos los archivos
git add .

# Verificar archivos en staging
git status
```

**Resultado:** Archivos en verde listos para commit

---

## 💾 Paso 5: Hacer el Primer Commit

```powershell
# Crear commit con mensaje descriptivo
git commit -m "Initial commit: Sistema CRUD Multiservicio con SOAP, gRPC y Sockets

- Implementación completa de 3 servicios en Java
- Servicio SOAP (JAX-WS) en puerto 8080
- Servicio gRPC (Protocol Buffers) en puerto 5001
- Servicio Socket TCP/IP (JSON) en puerto 5002
- Documentación completa
- Casos de prueba exhaustivos
- Scripts SQL incluidos"

# Verificar el commit
git log --oneline
```

---

## 🚀 Paso 6: Subir a GitHub

```powershell
# Renombrar rama a main (si es necesario)
git branch -M main

# Push al repositorio remoto
git push -u origin main
```

### Si pide autenticación:
1. **Usuario:** CamilaParedes23
2. **Contraseña:** Token de acceso personal (PAT)

### Si no tienes el Token PAT:
1. Ve a GitHub.com → Settings → Developer settings
2. Personal access tokens → Tokens (classic)
3. Generate new token
4. Selecciona permisos: `repo` (todos)
5. Copia el token generado
6. Úsalo como contraseña

---

## ✅ Paso 7: Verificar en GitHub

1. Abre: https://github.com/CamilaParedes23/CamilaParedes-Leccion1
2. Deberías ver todos los archivos
3. Verifica que el README.md se muestre correctamente
4. Revisa que todas las carpetas estén presentes

---

## 📁 Estructura Esperada en GitHub

```
CamilaParedes-Leccion1/
├── .gitignore
├── README.md
├── PRUEBAS.md
├── ENTREGABLES.md
├── GITHUB.md
├── database/
│   └── schema.sql
├── SOAP-Java/
│   ├── README.md
│   ├── pom.xml
│   └── src/
├── RPC-Java/
│   ├── README.md
│   ├── pom.xml
│   └── src/
└── Sockets-Java/
    ├── README.md
    ├── pom.xml
    └── src/
```

---

## 🔄 Comandos para Actualizaciones Futuras

### Hacer cambios y subirlos

```powershell
# Ver archivos modificados
git status

# Añadir archivos modificados
git add .

# O añadir archivos específicos
git add archivo.java

# Commit con mensaje
git commit -m "Descripción del cambio"

# Subir a GitHub
git push origin main
```

### Ver historial de commits

```powershell
git log --oneline --graph --all
```

### Descargar cambios del repositorio

```powershell
git pull origin main
```

---

## 🐛 Solución de Problemas

### Error: "failed to push some refs"

**Causa:** El repositorio remoto tiene cambios que no tienes localmente

**Solución:**
```powershell
git pull origin main --rebase
git push origin main
```

---

### Error: "Permission denied (publickey)"

**Causa:** Problema de autenticación

**Solución:** Usa HTTPS en lugar de SSH
```powershell
git remote set-url origin https://github.com/CamilaParedes23/CamilaParedes-Leccion1.git
```

---

### Error: "The requested URL returned error: 403"

**Causa:** Credenciales incorrectas

**Solución:**
1. Genera un Personal Access Token en GitHub
2. Úsalo como contraseña
3. O configura Git Credential Manager

---

### Verificar qué archivos se ignorarán

```powershell
git status --ignored
```

---

### Eliminar archivos del staging (antes de commit)

```powershell
git reset HEAD archivo.java
```

---

### Ver diferencias antes de commit

```powershell
git diff
```

---

## 📊 Checklist Final

Antes de compartir el repositorio, verifica:

- [ ] README.md visible y formateado correctamente
- [ ] Todos los proyectos Java presentes (SOAP, gRPC, Sockets)
- [ ] Archivos .gitignore funcionando (no hay carpetas target/)
- [ ] Documentos PRUEBAS.md y ENTREGABLES.md visibles
- [ ] Script SQL en carpeta database/
- [ ] Archivos pom.xml de los 3 proyectos
- [ ] Archivo .proto del proyecto gRPC
- [ ] Sin archivos .NET antiguos
- [ ] Sin carpetas .idea/ ni target/

---

## 🎓 Comandos Resumen (Copiar y Pegar)

```powershell
# 1. Navegar al proyecto
cd c:\Users\usuario\Documents\UNIVERSIDAD\7MO\DISTRIBUIDAS\PRIMERO\PRUEBA

# 2. Inicializar Git
git init

# 3. Configurar remoto
git remote add origin https://github.com/CamilaParedes23/CamilaParedes-Leccion1.git

# 4. Añadir archivos
git add .

# 5. Commit
git commit -m "Initial commit: Sistema CRUD Multiservicio con SOAP, gRPC y Sockets"

# 6. Subir a GitHub
git branch -M main
git push -u origin main
```

---

## 🔍 Verificar que Todo Está Bien

```powershell
# Ver el estado del repositorio
git status

# Ver los commits
git log --oneline

# Ver el remoto configurado
git remote -v

# Ver las ramas
git branch -a
```

---

## 📞 Compartir el Repositorio

Una vez subido, puedes compartir el link:

**URL del repositorio:**  
https://github.com/CamilaParedes23/CamilaParedes-Leccion1

**URL para clonar:**
```bash
git clone https://github.com/CamilaParedes23/CamilaParedes-Leccion1.git
```

---

## 🎉 ¡Listo!

Tu proyecto está ahora en GitHub y cualquier persona con el link puede:
1. Ver el código
2. Clonar el repositorio
3. Leer la documentación
4. Ejecutar los servicios

---

**Fecha de creación:** 7 de noviembre de 2025  
**Repositorio:** https://github.com/CamilaParedes23/CamilaParedes-Leccion1.git  
**Proyecto:** Sistema CRUD Multiservicio - Sistemas Distribuidos
