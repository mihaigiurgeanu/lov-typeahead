(ns lov-typeahead.directive
  (:require [lov-typeahead.dataset :as d]))

(defn link-typeahead 
  "The directive linking function that configs the typeahead element"
  [scope element attrs] (let [value-key (.-lovValueKey attrs)
                              lov-model (.-lovModel attrs)
                              dataset-is-valid-attr (.-lovDatasetIsValid attrs)
                              dataset-is-valid (if (nil? dataset-is-valid-attr) true
                                                 (.$eval scope dataset-is-valid-attr))
                              update-model (fn [event datum name]
                                             (.log js/console (str "update model, dataset name: " name))
                                             (.log js/console (str "update model, datum: " (.stringify js/JSON datum)))
                                             (.log js/console (str "update model, datum.object: " (.stringify js/JSON (.-object datum))))
                                             (.log js/console (str "update model, lov-model: " lov-model))
                                             (.log js/console (str "update model, value before update: " (.stringify js/JSON (aget scope lov-model))))
                                             (aset scope lov-model (.-object datum))
                                             (.log js/console (str "update model, value after update: " (.stringify js/JSON (aget scope lov-model))))
                                             (.$digest scope))
                              set-up-typeahead (fn []
                                                 (.log js/console (str "dataset name: " (.-lovTypeahead attrs)))
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
                                                           no-nill-assoc (fn 
                                                                           ([options key value] 
                                                                             (if (nil? value) options
                                                                               (assoc options key (clj->js value))))
                                                                           ([options key test value] 
                                                                             (if (nil? test) options
                                                                               (assoc options key (clj->js value)))))
                                                           options (no-nill-assoc options :prefetch prefetch {:url prefetch, :filter filter-fn})
                                                           options (no-nill-assoc options :remote remote {:url remote, :filter filter-fn})
                                                           options (no-nill-assoc options :limit limit)
                                                           options-js (clj->js options)]
                                                       (.log js/console (str "prefetch: " prefetch))
                                                       (.log js/console (str "options: " (.stringify js/JSON options-js)))
                                                       options-js))))]
                          (.$observe attrs "lovTypeahead" set-up-typeahead)
                          (.$observe attrs "lovRemote" set-up-typeahead)
                          (.$watch scope lov-model (fn [value] (.val element (aget value value-key))))
                          (doto element
                            (.on "typeahead:selected" update-model)
                            (.on "typeahead:autocompleted" update-model))))

(def lovTypeaheadModule
   "The definition of the lov-typeahead's module" 
  (.module js/angular "lovTypeahead" (clj->js [])))

(.directive lovTypeaheadModule
  "lovTypeahead"
  (fn []
    (clj->js {:link link-typeahead})))

