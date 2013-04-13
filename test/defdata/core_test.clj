(ns defdata.core-test
  (:use midje.sweet)
  (:require defdata.core-defs
            defdata.core)
  (:import java.net.FileNameMap))

;; FileThing is defined in defdata/core_defs.clj to ensure things work across namespaces
(fact (instance? FileNameMap (defdata.core-defs/new-FileThing 5))=> true)
(fact (instance? defdata.core_defs.FileThing (defdata.core-defs/new-FileThing 5)) => true)
(fact (defdata.core-defs/new-FileThing "foo") => (throws AssertionError))

(fact (instance? defdata.core_defs.Foo (defdata.core-defs/new-Foo 5)) => true)
(fact (defdata.core-defs/new-Foo "foo") => (throws AssertionError))

(fact (defdata.core/non-empty-string? nil) => false)
(fact (defdata.core/non-empty-string? "") => false)
(fact (defdata.core/non-empty-string? "  ") => false)
(fact (defdata.core/non-empty-string? "i") => true)
