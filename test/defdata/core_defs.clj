(ns defdata.core-defs
  (:use defdata.core)
  (:import java.net.FileNameMap))

;; different namespace to ensure the code works across namespaces
;; will generate a defrecord FileThing and a creation defn new-FileThing that asserts all inputs are valid
(defdata FileThing [a]
  {:a number?} ;; vector with validation assertions against creation arguments
  FileNameMap ;; protocols/interfaces implemented
  (getContentTypeFor [this fileName] (str a "-" fileName))) ;; implemented protocol/interface methods

(defdata Foo [a]
  {:a number?})

(defdata Bar [a b]
  {:a number?})