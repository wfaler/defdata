(ns defdata.core-test
  (:use midje.sweet)
  (:require defdata.core-defs
            defdata.core)
  (:import java.net.FileNameMap))

;; FileThing is defined in defdata/core_defs.clj to ensure things work across namespaces
(fact (instance? FileNameMap (defdata.core-defs/new-FileThing 5))=> true)
(fact (instance? defdata.core_defs.FileThing (defdata.core-defs/new-FileThing 5)) => true)
(fact (defdata.core-defs/new-FileThing "foo") => (throws AssertionError))
;; a type that does not implement any protocols
(fact (instance? defdata.core_defs.Foo (defdata.core-defs/new-Foo 5)) => true)
(fact (defdata.core-defs/new-Foo "foo") => (throws AssertionError))
;; a type that takes multiple arguments
(fact (instance? defdata.core_defs.Bar (defdata.core-defs/new-Bar 2 2)) => true)

(fact (defdata.core/validate defdata.core-defs/constraints-Foo {:a 5}) => {:right {:a 5}})
(fact (defdata.core/validate defdata.core-defs/constraints-Foo {:a "Foo"}) => {:left [:a]})

(fact (defdata.core/valid? defdata.core-defs/constraints-Foo {:a 5}) => true)
(fact (defdata.core/valid? defdata.core-defs/constraints-Foo {:a "5"}) => false)

(fact ((defdata.core/valid? defdata.core-defs/constraints-Foo) {:a 5}) => true)
(fact ((defdata.core/valid? defdata.core-defs/constraints-Foo) {:a "5"}) => false)

(fact (defdata.core/fold {:left 2} (partial + 2) (partial - 2)) => 4)
(fact (defdata.core/fold {:right 2} (partial + 2) (partial - 2)) => 0)

(fact (defdata.core/non-empty-string? nil) => false)
(fact (defdata.core/non-empty-string? "") => false)
(fact (defdata.core/non-empty-string? "  ") => false)
(fact (defdata.core/non-empty-string? "i") => true)
