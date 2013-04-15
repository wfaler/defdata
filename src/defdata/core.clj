(ns defdata.core
  (:require [clojure.string]))

(defn non-empty-string? [s]
  "asserts whether an input is a String and non-empty (after trimming), returns true or false"
  (not (clojure.string/blank? s)))

(defn- validate-key [kw value constraints]
  (if (not= nil (kw constraints))
    ((kw constraints) (kw value))
    false))

(defn validate [validation-set value]
  "takes a record or map, and a map containing constraints in the form {:key validation-fn}
  returns either {:right value} on success or {:left keys-with-errors} on failure"
  (if (empty? (keys validation-set))
    {:valid true}
    (let [invalid-keys (filter #(not (validate-key %1 value validation-set)) (keys validation-set))]
              (if (empty? invalid-keys)
                {:right value}
                {:left (vec invalid-keys)}))))

(defn valid?
  ([constraints] (partial valid? constraints))
  ([constraints value] (nil? (:left (validate constraints value)))))

(defn fold [fail-fn success-fn either]
  (if (:right either)
    (success-fn (:right either))
    (fail-fn (:left either))))


(defmacro defdata
  "Takes a name for a defrecord to be created, it's fields, a map of constraints for the fields like {:key validator-fn?}
  and optional defrecord options such as interfaces/protocols implemented and their methods.
  Creates the defrecord and additionally a new-[RecordName] defn which validates inputs before creating a record,
  either returning a new record or throwing an AssertionError.
  Also binds the constraints constraints-[type] which can then be used as input to the validate function"
  ([type-name field-list constraint-map & etc]
     `(do
        (defrecord ~type-name ~field-list ~@etc)
        (def ~(symbol (str "constraints-" type-name)) ~constraint-map)
        (defn ~(symbol (str "new-" type-name)) ~field-list
          (let [value# (new ~type-name ~@field-list)
                validations# (validate ~(symbol (str "constraints-" type-name)) value#)]
            (if (:right validations#)
              value#
              (assert false (str "cannot create new " ~type-name ", one or more of its arguments are invalid: " ~field-list))
              )
            )
          )
         )))