defdata
=======

Clojure experimentation in self-validating data types.

## Usage

A definition such as:

  (defdata MyDataType [name age] 
    {:name non-empty-string? :age number?} ;; map with validation functions for each key in a concrete instance
    SomeProtocol ;; optional protocol/interface implementations
      (some-protocol-fn-impl [a] (println a)) ;; implementation of protocol functions
  )

Will generate the following:
* A defrecord with the given properties and protocol implementations
* A new-MyDataType function, taking (in this case) name and age args, returning a MyDataType record if inputs are valid, throwing an AssertionError if any arguments are invalid
* A validate-MyDataType function that takes a MyDataType OR a map as an argument, returning {:valid true} if the input is valid or {:valid false :errors list-of-properties-that-are-invalid}