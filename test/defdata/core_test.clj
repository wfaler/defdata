(ns defdata.core-test
  (:use midje.sweet)
  (:require defdata.core-defs)
  (:import java.net.FileNameMap))

;; FileThing is defined in defdata/core_defs.clj to ensure things work across namespaces
(fact (instance? FileNameMap (defdata.core-defs/new-FileThing 5))=> true)
(fact (instance? defdata.core_defs.FileThing (defdata.core-defs/new-FileThing 5)) => true)
(fact (defdata.core-defs/new-FileThing "foo") => (throws AssertionError))