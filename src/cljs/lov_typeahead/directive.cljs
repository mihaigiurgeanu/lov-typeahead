(ns lov-typeahead.directive
  (:require [lov-typeahead.dataset :as d]))

(defn no-nill-assoc
  ([options key value] 
    (if (nil? value) options
      (assoc options key value)))
  ([options key test value] 
    (if (nil? test) options
      (assoc options key value))))

(defn link-typeahead 
  "The directive linking function that configs the typeahead element"
  [scope element attrs] (let [value-key (.-lovValueKey attrs)
                              lov-model (.-lovModel attrs)
                              fields (clojure.string/split lov-model #"\.")
                              model-parent (do
                                             (.log js/console (str "lov-model fields: " (.stringify js/JSON (clj->js fields))))
                                             (.log js/console (str "from scope"))
                                             (loop [crt scope
                                                    fields' fields]
                                               (if (>= 1 (count fields'))
                                                 crt
                                                 (do
                                                   (.log js/console (str "aget " (first fields')))
                                                   (recur (aget crt (first fields'))
                                                          (rest fields'))))))
                              model-field (last fields)
                              update-model (fn [event datum name]
                                             (.log js/console (str "update model, dataset name: " name))
                                             (.log js/console (str "update model, datum: " (.stringify js/JSON datum)))
                                             (.log js/console (str "update model, datum.object: " (.stringify js/JSON (.-object datum))))
                                             (.log js/console (str "update model, lov-model: " lov-model))
                                             (.log js/console (str "update model, value before update: " (.stringify js/JSON (.$eval scope lov-model))))
                                             (.log js/console (str "model field: " model-field))
                                             (aset model-parent model-field (.-object datum))
                                             (.log js/console (str "update model, value after update: " (.stringify js/JSON (.$eval scope lov-model))))
                                             (.$digest scope))
                              set-up-typeahead (fn []
                                                 (let [dataset-is-valid-attr (.-lovDatasetIsValid attrs)
                                                       dataset-is-valid (if (nil? dataset-is-valid-attr) true
                                                                          (.$eval scope (str "(" dataset-is-valid-attr ")? true:false")))]
                                                   (.log js/console (str "dataset name: " (.-lovTypeahead attrs)))
                                                   (.log js/console (str "dataset-is-valid: " dataset-is-valid))
                                                   (.typeahead element "destroy")
                                                   (when dataset-is-valid
                                                     (.log js/console "setup the typehead")
                                                     (.typeahead element 
                                                       (let [name (.-lovTypeahead attrs)
                                                             limit (.-lovLimit attrs)
                                                             prefetch (.-lovPrefetch attrs)
                                                             remote (.-lovRemote attrs)
                                                             options {:name name}
                                                             filter-fn #(d/json->dataset value-key %)
                                                             options (no-nill-assoc options :prefetch prefetch {:url prefetch, :filter filter-fn})
                                                             options (no-nill-assoc options :remote remote {:url remote, :filter filter-fn})
                                                             options (no-nill-assoc options :limit limit)
                                                             options-js (clj->js options)]
                                                         (.log js/console (str "prefetch: " prefetch))
                                                         (.log js/console (str "options: " (.stringify js/JSON options-js)))
                                                         options-js)))))]
                          (.log js/console "lov-typeahead directive linking")
                          (.$observe attrs "lovTypeahead" set-up-typeahead)
                          (.$observe attrs "lovRemote" set-up-typeahead)
                          (.$watch scope lov-model (fn [value] (.val element (if (or (nil? value) (undefined? value)) 
                                                                               "" 
                                                                               (aget value value-key)))))
                          (doto element
                            (.on "typeahead:selected" update-model)
                            (.on "typeahead:autocompleted" update-model))))

(def lovTypeaheadModule
   "The definition of the lov-typeahead's module"
   (.module js/angular "lovTypeahead" (clj->js [])))

(.directive lovTypeaheadModule
  "lovTypeahead"
  (fn []
    (.log js/console "lovTypeahead directive definition")
    (clj->js {:link link-typeahead})))

