# Kyaml

## Kyaml is a simple, flexible, and forgiving YAML parser for Android.

<table>
<tr>
<th>Kotlin implementation</th>
<th>YAML file</th>
</tr>
<tr>
<td>

```kotlin
Kyaml(
    activity = this,
    yamlResourceIdInRaw = R.raw.test,
    // Or if in assets: yamlFileNameInAssets = "test.yaml",
    onEachItem = { key, value ->
        when (key) {
            "foo" -> value // "Kyaml!"
            "bar" -> value // 123
            // ...
        }
    }
)
```

</td>
<td>

```yaml
# /app/src/main/res/raw/test.yaml

foo: Kyaml!
bar: 123
baz: true
```

</td>
</tr>
</table>

### Pass a YAML file from `/assets/` or `/res/raw/` to return parsed key/values for each item. Each value returned will be casted as the type found.
### &nbsp;&nbsp;<u>Examples of value types returned from Kyaml</u>: 
- key1: test! = `String`
- key2: 123 = `Int`
- key3: 2147483648 = `Long`
- key4: [ 1, 2, 3 ] = `List of Integers`

<br>

#### Check out the [example app for Kyaml](/example-kyaml/)!

<br>

## Table of Contents
- [Usage / Examples](#usage--examples)
- [Syntax](#syntax)
- [Installation](#installation)
- [Versioning](#versioning)
- [License](#license)

<br>

## Usage / Examples

### Ex 1: YAML in raw directory gathering primative types and key nesting.

<table>
<tr>
<th>Kotlin implementation</th>
<th>YAML file</th>
</tr>
<tr>
<td>

```kotlin
Kyaml(
    activity = this,
    yamlResourceIdInRaw = R.raw.ex1,
    onEachItem = { key, value ->
        when (key) {
            "isExample1" -> value // true
            "strings.name" -> value // "example1"
            "strings.location" -> value // "raw"
            "numbers.intNum" -> value // 123
            "numbers.floatNum" -> value // 987.65
        }
    }
)
```
  
</td>
<td>

```yaml
# /app/src/main/res/raw/ex1.yaml

isExample1: true
strings:
  name: example1
  location: "raw"
numbers: { intNum: 123, floatNum: 987.65 }
```

</td>
</tr>
</table>

<br>

### Ex 2: YAML in assets directory gathering sequences.

<table>
<tr>
<th>Kotlin implementation</th>
<th>YAML file</th>
</tr>
<tr>
<td>

```kotlin
Kyaml(
    activity = this,
    yamlFileNameInAssets = "ex2.yaml",
    onEachItem = { key, value ->
        when (key) {
            "tags" -> {
                (value as? List<*>)?.let {
                    value.forEach {
                        // it = "foo"
                        // it = "bar"
                        // it = "baz"
                    }
                }
            }
            "intervals" -> {
                (value as? List<*>)?.let {
                    value.forEach {
                        // it = 15
                        // it = 30
                        // it = 45
                    }
                }
            }
        }
    }
)
```
  
</td>
<td>

```yaml
# /app/src/main/assets/ex2.yaml

tags:
  - foo
  - bar
  - baz
intervals: [ 15, 30, 45 ]
```

</td>
</tr>
</table>

<br>

### Ex 3: YAML file note found. Error cancels Kyaml.

<table>
<tr>
<th>Kotlin implementation</th>
<th>YAML file</th>
</tr>
<tr>
<td>

```kotlin
Kyaml(
    activity = this,
    yamlFileNameInAssets = "not_found.yaml",
    onEachItem = { key, value ->
        // Error loading YAML file.
        // contents in onEachItem
        // will never be called.
    },
    onError = {
        when (it) {
            is IllegalArgumentException -> 
                // "Could not open not_found.yaml"
                it.message
            else -> it.message
        }
    }
)
```
  
</td>
<td>

```yaml
"not_found.yaml" does not exist in the project.
```

</td>
</tr>
</table>

<br>

## Syntax

Usage is simply calling [Kyaml()](#usage--examples) passing up to four arguments (3 required and 1 optional) into its constructor. There are 2 public constructors that vary only by 1 argument:

- <b>`activity`</b> - Activity / <i><b>Required</b></i>
  - Active Activity.
- <b>`yamlResourceIdInRaw`</b> or <b>`yamlFileNameInAssets`</b> - <i><b>Only 1 Required</b></i>
  - <b>`yamlResourceIdInRaw`</b> - Int (@RawRes) / <i><b>Required</b></i>
    - YAML file located in `/res/raw/` to consume.

    <b><u>OR</u></b>
    
  - <b>`yamlFileNameInAssets`</b> - String / <i><b>Required</b></i>
    - YAML file name located in `/assets/` to consume. Optionally the extension can be included with the file name.
- <b>`onEachItem`</b> - (String, Any?) -> Unit / <i><b>Required</b></i>
  - Called on every item parsed from the YAML file. Included will be the key and value. The key is always of type `String`. The value will be of the detected type when parsed. This may include `Int`, `Long`, `Float`, `Double`, `Boolean`, `String`, or `null`. The value may also be a `List` of any of these types.
- <b>`onError`</b> - (Exception) -> Unit / <i><b>Optional</b></i>
  - Any exception caught within Kyaml will stop the process and pass the exception here. An `IllegalArgumentException` will be thrown if `yamlFileNameInAssets` contains a file name that could not be found. It is not known if any other exception will be thrown when using Kyaml.

<br>

## Installation

### Install with AAR and gradle (Local)
1) Download the latest [kyaml.aar](kyaml.aar).
2) Move `kyaml.aar` to your project's `libs` directory (Example: `YourProject/app/libs/`).
3) In your `build.gradle`, add <b>only one</b> of the following to your `dependencies { }`:
- ```groovy
  // adds only kyaml.aar
  implementation fileTree(dir: "libs", include: ["kyaml.aar"])
  
  // OR

  // adds all .aar files in your libs directory.
  implementation fileTree(dir: "libs", include: ["*.aar"]) 
  ```
4) [Sync gradle](https://www.delasign.com/blog/how-to-sync-an-android-project-with-its-gradle-files-in-android-studio/) successfully.
5) Done! Your Android project is now ready to use Kyaml. Go to [Usage / Examples](#usage--examples) or [Syntax](#syntax) for Kyaml usage!

### Install with gradle (Remote)
>Coming soon!

<br>

## Versioning
- [SemVer](http://semver.org/) is used for versioning.
- Given a version number MAJOR . MINOR . PATCH
    1) MAJOR version - Incompatible API changes.
    2) MINOR version - Functionality added in a backwards-compatible manner.
    3) PATCH version - Backwards-compatible bug fixes.
       <br><br>

## License
Kyaml created by Adam Steinberg of DIGIDEMIC, LLC
```
Copyright 2024 DIGIDEMIC, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```