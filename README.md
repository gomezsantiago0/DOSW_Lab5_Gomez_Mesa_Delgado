# SkyRescue TDD

## Integrantes

- Nicolas Delgado
- Santiago Gomez
- Diego Mesa

## Descripción de SkyRescue

SkyRescue es una plataforma de coordinación de drones para operaciones de emergencia urbana.
Resuelve el problema de gestionar eficientemente drones que transportan kits médicos, cámaras
térmicas y radios hacia zonas de difícil acceso. Las reglas principales del dominio garantizan
que un dron ocupado no pueda ser reasignado, que la distancia de la misión no supere la autonomía
del dron, que un operador no pueda tener dos misiones activas simultáneamente y que una misión
completada no pueda cerrarse nuevamente. Las tres operaciones desarrolladas con TDD son:
`addDrone`, que registra un dron en el centro; `assignMission`, que asigna una misión a un
operador y un dron disponible; y `completeMission`, que cierra una misión activa y libera el dron.

## Evidencia TDD

### Ciclo TDD - completeMission

**RED:** prueba que demuestra que `completeMission` falla sin implementación.

![Prueba fallando](docs/evidence/tdd-red.png)

**GREEN:** implementación mínima que hace pasar las pruebas.

![Prueba pasando](docs/evidence/tdd-green.png)

**REFACTOR:** se extrajo el método privado `findMissionById` para simplificar y mejorar
la legibilidad de `completeMission`, sin modificar su comportamiento.

## Evidencia de cobertura

### Primera ejecución
![Cobertura inicial](docs/evidence/coverage-first.png)

### Cobertura final
![Cobertura final](docs/evidence/coverage-final.png)

## Análisis estático - SonarQube

El análisis se ejecutó localmente con SonarQube Community Build (26.9.0.129388) sobre el
proyecto `skyrescue-tdd`, integrado mediante `sonar-maven-plugin` y el reporte de cobertura
de JaCoCo.

**Resultados:**
- Quality Gate: **Passed** ✅
- Cobertura: **87.1%** (101 líneas a cubrir)
- Duplicaciones: 0.0%
- Issues abiertos: 0 Security, 2 Reliability, 21 Maintainability

![Dashboard SonarQube](docs/evidence/sonarqube-dashboard.png)

## Pull Requests

- PR JUnit: [#1](https://github.com/gomezsantiago0/DOSW_Lab5_Gomez_Mesa_Delgado/pull/1)
- PR clases base: [#2](https://github.com/gomezsantiago0/DOSW_Lab5_Gomez_Mesa_Delgado/pull/3)
- PR TDD addDrone: [#3](https://github.com/gomezsantiago0/DOSW_Lab5_Gomez_Mesa_Delgado/pull/6)
- PR TDD assignMission: [#4](https://github.com/gomezsantiago0/DOSW_Lab5_Gomez_Mesa_Delgado/pull/5)
- PR TDD completeMission: [#5](https://github.com/gomezsantiago0/DOSW_Lab5_Gomez_Mesa_Delgado/pull/4)
- PR JaCoCo: [#6](https://github.com/gomezsantiago0/DOSW_Lab5_Gomez_Mesa_Delgado/pull/11)
- PR SonarQube: [#7](https://github.com/gomezsantiago0/DOSW_Lab5_Gomez_Mesa_Delgado/pull/12)

## Reflexión técnica

### 1. ¿Qué error o comportamiento inesperado fue detectado primero gracias a una prueba?:
Al desarrollar assignMission con TDD, la prueba shouldThrowIllegalStateExceptionWhenOperatorAlreadyHasActiveMission reveló que, sin la validación explícita, el sistema permitía asignar una segunda misión activa al mismo operador. Esto evidenció que el estado del operador (no solo el del dron) debía verificarse antes de crear una nueva misión.

### 2. ¿Qué parte del código cambió durante REFACTOR sin modificar el comportamiento?:
En completeMission se extrajo la búsqueda de la misión a un método privado findMissionById, que antes estaba mezclada con la lógica de validación y cambio de estado. Esto simplificó el método principal y centralizó el manejo del caso "misión no encontrada" (lanzando IllegalArgumentException), sin alterar el comportamiento observado por las pruebas, que siguieron pasando sin cambios.

### 3. ¿Qué casos adicionales aparecieron al revisar la cobertura?:
Al revisar el reporte de JaCoCo no se identificaron casos adicionales pendientes: la cobertura de líneas ya superaba el 85% requerido desde la primera ejecución (coverage-first.png y coverage-final.png reflejan el mismo resultado, "All coverage checks have been met"), gracias a que el ciclo TDD nos obligó a escribir pruebas para cada rama de validación (addDrone, assignMission y completeMission) antes de implementar la lógica.

### 4. ¿Qué hallazgo de SonarQube produjo un cambio real en el código?:
SonarQube reportó Quality Gate Passed, 87.1% de cobertura y 23 issues abiertos (2 Reliability, 21 Maintainability, 0 Security, 0.0% duplicaciones). Ninguno era crítico, por lo que se documentaron como deuda técnica pendiente sin requerir cambios inmediatos en el código.