(ns lov-typeahead.directive
  (:require [lov-typeahead.dataset :as d]))

(defn set-up-typeahead 
  "The directive linking function that configs the typeahead element"
  [scope element attrs] (let [name (.-lovTypeahead attrs)
                              prefetch (.-lovPrefetch attrs)
                              remote (.-lovRemote attrs)
                              limit (.-lovLimit attrs)
                              value-key (.-lovValueKey attrs)
                              lov-model (.-lovModel attrs)
                              update-model (fn [event datum name]
                                             (.log js/console (str "update model, dataset name: " name))
                                             (.log js/console (str "update model, datum: " (.stringify js/JSON datum)))
                                             (.log js/console (str "update model, datum.object: " (.stringify js/JSON (.-object datum))))
                                             (.log js/console (str "update model, lov-model: " lov-model))
                                             (.log js/console (str "update model, value before update: " (.stringify js/JSON (aget scope lov-model))))
                                             (aset scope lov-model (.-object datum))
                                             (.log js/console (str "update model, value after update: " (.stringify js/JSON (aget scope lov-model))))
                                             (.$digest scope))]
                                                      (.log js/console (str "prefetch: " prefetch))
                                                      (doto element
                                                        (.typeahead (let [options {:name (str name "-123")}
                                                                          filter-fn #(d/json->dataset value-key %)
                                                                          options (if (nil? prefetch) options 
                                                                                    (assoc options :prefetch (clj->js {:url prefetch
                                                                                                                       :filter filter-fn})))
                                                                          options (if (nil? remote) options
                                                                                    (assoc options :remote (clj->js {:url remote
                                                                                                                     :filter filter-fn})))]
                                                                      (clj->js options)))
                                                        (.on "typeahead:selected" update-model)
                                                        (.on "typeahead:autocompleted" update-model)
                                                        (.on "$destroy" (fn [] 
                                                                          (.log js/console "destroy evenet on typeahead")
                                                                          (.typeahead('destroy')))))))

(def lovTypeaheadModule
   "The definition of the lov-typeahead's module" 
  (.module js/angular "lovTypeahead" (clj->js [])))

(.directive lovTypeaheadModule
  "lovTypeahead"
  (fn []
    (clj->js {:link set-up-typeahead})))

