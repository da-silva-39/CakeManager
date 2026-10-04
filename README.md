# CakeManager

Aplicação Android para a gestão da confeitaria **Bolos da Axcila**.

## Descrição

O CakeManager permite gerir produtos, categorias, ingredientes, receitas,
stock e encomendas através de uma interface simples e moderna, baseada em
Material Design.

## Objetivos

- Centralizar a gestão da confeitaria.
- Controlar stock de ingredientes com alertas de stock baixo.
- Gerir encomendas com cálculo automático de valores (MZN).
- Apresentar um dashboard com indicadores essenciais.

## Tecnologias

- Android SDK (minSdk 35, targetSdk/compileSdk 37)
- Java (sem Kotlin, sem Compose)
- Layouts XML
- Gradle (Kotlin DSL)
- Room Database (Entities, DAOs, Database, Repositories)
- AndroidX: AppCompat, Material Components, RecyclerView, CardView, Lifecycle
- Arquitetura MVVM

## Arquitetura

```
app/src/main/java/com/bolosdaaxcila/cakemanager/
├── data/
│   ├── local/        # AppDatabase, DAOs
│   ├── model/        # Entidades Room
│   └── repository/   # Repositories (ExecutorService + LiveData)
├── ui/
│   ├── auth/         # Splash, Login, Register
│   ├── dashboard/    # Dashboard
│   ├── categories/   # CRUD de categorias
│   ├── products/     # CRUD de produtos
│   ├── ingredients/  # CRUD de ingredientes
│   ├── recipes/      # CRUD de receitas
│   ├── stock/        # Movimentos e alertas
│   └── orders/       # Encomendas e itens
└── utils/            # PasswordUtils, SessionManager, MoneyUtils
```

## Funcionalidades

- Cadastro e autenticação com hash de palavra-passe (SHA-256)
- Dashboard com totais e alertas de stock baixo
- CRUD de categorias, produtos, ingredientes, receitas e encomendas
- Pesquisa em produtos, categorias, ingredientes, receitas e encomendas
- Controlo de stock (entradas/saídas, histórico, bloqueio de stock negativo)
- Cálculo automático de subtotais e total com BigDecimal
- Estados de encomenda: Pendente, Confirmada, Em preparação, Pronta, Entregue
- Validação de formulários e confirmações de eliminação

## Como executar

1. Abrir o projeto no Android Studio.
2. Sincronizar o Gradle.
3. Executar num emulador ou dispositivo (Android 15+).

Via linha de comandos:

```bash
./gradlew :app:installDebug
```

## Como gerar APK

```bash
./gradlew :app:assembleDebug
# APK em app/build/outputs/apk/debug/app-debug.apk
```

## Requisitos

- JDK 21
- Android SDK com compileSdk 37
- Gradle 9.5 (wrapper incluído)
