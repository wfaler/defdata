(ns defdata.core)


(defmacro defdata
  ([type-name field-list constraints & etc]
;;`(defrecord2 ~type-name ~field-list
;; invoke defrecord2 with default constructor function name
;;~(symbol (str "new-" (.toLowerCase (str type-name))))))
     `(do
        (defrecord ~type-name ~field-list ~@etc)
        (defn ~(symbol (str "new-" type-name)) ~field-list
          (println "hello world"))
;; define the constructor functions
;; setup pprinting
        )))


(defn non-empty-string? [s]
  (and (string? s) (< 0 (.length (.trim s)))))