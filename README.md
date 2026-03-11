# Jenkins Project Activity Indicator Plugin

## Overview

**Project Activity Indicator Plugin** is a lightweight Jenkins plugin that shows who is currently working with a specific project.

The plugin **does not lock or restrict CI/CD pipelines**.
It only provides a **visual indicator** to inform other team members that someone is currently interacting with the project.

This helps reduce conflicts when multiple developers or DevOps engineers work with the same Jenkins job.

Example UI:

* Displays the user currently working with the project
* Shows the project as **"occupied"**
* Allows manual release via **"Unlock Project"**

This is purely **informational** and does not interfere with Jenkins job execution.

---

## Features

* Shows who is currently working with a Jenkins project
* Visual indicator in the project UI
* Manual **Unlock Project** option
* Does **not block builds**
* Does **not affect CI/CD pipelines**
* Lightweight and safe for production environments

---

## Compatibility

Tested with:

* **Jenkins:** 2.541.1
* **Java:** OpenJDK 21
* **Maven:** 3.9.13

The plugin should work with most modern Jenkins LTS versions that support Java 21.

---

## Build

To build the plugin:

```bash
mvn clean package
```

Build without running tests:

```bash
mvn clean install -DskipTests
```

The compiled plugin (`.hpi`) will be located in:

```
target/*.hpi
```

---

## Installation

1. Build the plugin using Maven.
2. Go to **Jenkins → Manage Jenkins → Plugins → Advanced**.
3. Upload the generated `.hpi` file.
4. Restart Jenkins.

---

## Usage

After installation:

1. Open any Jenkins project.
2. When a user starts interacting with the project, their name will appear as the active user.
3. Other users will see that the project is **currently occupied**.
4. The project can be manually released using the **Unlock Project** option.

The plugin **does not prevent builds or deployments** — it only informs users.

---

## Use Cases

Typical scenarios where this plugin is useful:

* Multiple DevOps engineers working in the same Jenkins project
* Avoiding accidental configuration overrides
* Indicating active maintenance or debugging
* Reducing conflicts during manual operations

---
## READY PLUGIN

jp_lock.hpi

## Contributing

Contributions are welcome.

Please follow the Jenkins community guidelines:

https://github.com/jenkinsci/.github/blob/master/CONTRIBUTING.md

---

## License

Licensed under **MIT License**.

See the `LICENSE.md` file for details.
