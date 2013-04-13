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
     (new-MyDataType "John Doe" 30) 
     constraints-MyDataType) ;; will return a {:right value}, where value is the initialised MyDataType

  (validate
     (new-MyDataType "" "foo")
     constraints-MyDataType) ;; will return a {:left [:name :age]} indicating failure of validation of name and age attributes
```
Additionally, the validate function can validate any map, in the above example if the input is a map that looks like a MyDataType it may pass validation if it contains all required attributes in valid form