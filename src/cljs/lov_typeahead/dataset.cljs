(ns lov-typeahead.dataset)

(defn to-datum
  "Transforms a business object into a typeahead datum object.
The business object is a map of fields to valuess. For example, for the business object represented by the map
{:id 1, :name \"Genesis Software Consulting\", :type \"Companny\"}, the typeahead datum structure will be:

{:value \"Genesis Software Consulting\",
 :tokens [\"Genesis\" \"Software\" \"Consulting\"]
 :object {:id 1, :name \"Genesis Software Consulting\", :type \"Companny\"}

In this case, you will call this function like this:

(to-datum \"name\" {:id 1, :name \"Genesis Software Consulting\", :type \"Companny\"}"
  [value-key biz-object]
  (let [value (get biz-object value-key)]
    {:value value
     :tokens (clojure.string/split value #"\s+")
     :object biz-object}))

(defn make-dataset
  "Converts a vector of businness objects to a vector of datums. The business objects are maps, mapping a field name to a value name.
value-key is the key that holds the searchable value in the business object. The typeahead will search on this field."
  [value-key objects]
  (map #(to-datum value-key %) objects))

(defn json->dataset 
  [value-key js-objects]
  (clj->js (make-dataset (keyword value-key) (js->clj js-objects :keywordize-keys true))))
