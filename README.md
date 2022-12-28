# Native Contacts


**Features**
=============
* Get all native Contacts with all details
* No need to ask permission direct call this function an get all contacts
* ✨Fast Working ✨

**Usage**
=============
```sh
ContactProvider.getNativeContacts(context) { contactList ->
    //this is native contactList
}
```

**Installation**
=============
Add it in your root build.gradle at the end of repositories:

```sh
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency
```sh
dependencies {
    implementation 'com.github.bhargavsavasani:NativeContacts:1.0.0'
}
```
