defdata
=======

Clojure experimentation in self-validating data types.

## Usage

A definition such as:

```clojure
  (defdata MyDataType [name age] 
    {:name non-empty-string? :age number?} ;; map with validation functions for each key in a concrete instance
    SomeProtocol ;; optional protocol/interface implementations
      (some-protocol-fn-impl [a] (println a)) ;; implementation of protocol functions
  )
```

The above will generate the following:
* A defrecord with the given properties and protocol implementations
* A new-MyDataType function, taking (in this case) name and age args, returning a MyDataType record if inputs are valid, throwing an AssertionError if any arguments are invalid:

```clojure
  (new-MyDataType "John Doe" 30) ;; will return an initialised MyDataType record

  (new-MyDataType "" "foo") ;; will throw an AssertionError as both inputs are invalid
```

Finally, a def holding the provided constraints for the record is bound to a name like "constraints-MyDataType". This can be used as input to the provided validate function:

```clojure
  (validate 
     {:name "John Doe" :age 30} 
     constraints-MyDataType) 
  ;; will return a {:right value}, where value is the initialised MyDataType

  (validate
     {:name "Wille" :age "foo"}
     constraints-MyDataType) 
  ;; will return a {:left [:age]} indicating failure of validation of the age attribute 
  ;; (in a vector in case there are multiple failures
```
Additionally, the validate function can validate any map, in the above example if the input is a map that looks like a MyDataType it may pass validation if it contains all required attributes in valid form.
As a note, the eagle-eyed may notice that the :left and :right notation for the return value of the validate function is borrowed from Haskells and Scalas Either-type.

### Validating nested data types

Assume you have a type Bar which has to have a type Foo. To validate the nested Foo or any other nested data types to any arbitrary depth, simply add the following to your constraints:

```clojure

  (defdata Bar [foo]
    {:foo (valid? constraints-Foo)})
```
In the above example the valid? function with constraints-Foo will generate a validation function for you. Additionally if you call valid? like (valid? constraints-Foo a-foo) it will return a boolean with true or false indicating whether the passed argument is valud.

### Dealing with validation failure or success

If you want to use the validate function, you can do the following:

```clojure
  (fold (validate constraints-Foo foo) failure-fn success-fn)
  ;; failure-fn deals with validation failures, taking a vector of error keys
  ;; success-fn deals with success taking the object under validation as its argument
  
```
