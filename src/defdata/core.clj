(ns defdata.core)


(defmacro defdata
  ([type-name field-list constraints & etc]
     `(do
        (defrecord ~type-name ~field-list ~@etc)
        (defn ~(symbol (str "new-" type-name)) ~field-list
          (if (every? true? ~constraints)
            (new ~type-name ~field-list)
            (assert false (str "cannot create new " ~type-name ", one of its arguments is invalid: " ~field-list))
            ))
        )))
;; TODO: let constraints be directly accessed
;; create separate validator function that returns {:valid true :errors []} or {:valid false :errors [failed-validations]} 

(defn non-empty-string? [s]
  (and (string? s) (< 0 (.length (.trim s)))))