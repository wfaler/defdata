(ns defdata.core)

(defn non-empty-string? [s]
  "asserts whether an input is a String and non-empty (after trimming), returns true or false"
  (and (string? s) (< 0 (.length (.trim s)))))

(defn validate-key [kw value constraints]
  (if (not= nil (kw constraints))
    ((kw constraints) (kw value))
    false))


(defmacro defdata
  "Takes a name for a defrecord to be created, it's fields, a map of constraints for the fields like {:key validator-fn?}
  and optional defrecord options such as interfaces/protocols implemented and their methods.
  Creates the defrecord and additionally a new-[RecordName] defn which validates inputs before creating a record,
  either returning a new record or throwing an AssertionError.
  Also creates a defn validate-[type] that validates objects and returns {:valid true} or {:valid false :errors list-of-keys}"
  ([type-name field-list constraint-map & etc]
     `(do
        (defrecord ~type-name ~field-list ~@etc)
        (defn ~(symbol (str "validate-" type-name)) [~type-name]
          (if (empty? (keys ~constraint-map))
            {:valid true}
            (let [invalid-keys# (filter #(not (validate-key %1 ~type-name ~constraint-map)) (keys ~constraint-map))]
              (if (empty? invalid-keys#)
                {:valid true}
                {:valid false :errors invalid-keys#}))))
        (defn ~(symbol (str "new-" type-name)) ~field-list
          (let [value# (new ~type-name ~@field-list)
                validations# (~(symbol (str "validate-" type-name)) value#)]
            (if (:valid validations#)
              value#
              (assert false (str "cannot create new " ~type-name ", one or more of its arguments are invalid: " ~field-list))
              )
            )
          )
         )))