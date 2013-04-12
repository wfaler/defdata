(ns defdata.core-defs
  (:use defdata.core)
  (:import java.net.FileNameMap))

;; different namespace to ensure the code works across namespaces

(defdata FileThing [a]
  [(number? a)]
  FileNameMap
  (getContentTypeFor [this fileName] (str a "-" fileName)))

