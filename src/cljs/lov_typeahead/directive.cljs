(ns lov-typeahead.directive
  (:require [lov-typeahead.dataset :as d])
  (:require-macros [lov-typeahead.macros :as m]))

(defn extract-safe-val
  "Computes parent[index]. If parent parent[index] is null assigns parent[index] = {} and returns new object."
  [parent index]
  (let [val (aget parent index)]
    (if (or (nil? val) (undefined? val))
      (let [newobj js-obj] 
        (aset parent index newobj)
        newobj)
      val)))

(def indexed-field-re
  "Regular expression for matching an indexed field (i.e. foo[goo]"
  #"([^\[]*)\[([^\]]*)\]")

(defn set-in-scope
  "Sets a value to an angular js model in a given scope"
  [scope model val] 
  (let [fields (clojure.string/split model #"\.")
        model-field-part (last fields)
        model-field (let [indexed-field (re-matches indexed-field-re model-field-part)]
                      (if indexed-field
                        (.$eval scope (indexed-field 2))
                        model-field-part))
        model-parent (do
                       (loop [crt scope
                              fields' fields]
                           (let [field-part (first fields')
                                 indexed-field (re-matches indexed-field-re field-part)]
                             (if indexed-field
                               (let [this-field-name (indexed-field 1)
                                     index-value (.$eval scope (indexed-field 2))
                                     vector-field (extract-safe-val crt this-field-name)]
                                 (if (>= 1 (count fields'))
                                   vector-field
                                   (recur (extract-safe-val vector-field index-value) (rest fields'))))
                               (if (>= 1 (count fields'))
                                 crt
                                 (recur (extract-safe-val crt field-part)
                                 (rest fields')))))))]
    (aset model-parent model-field val)))

(defn templated-link-typeahead 
  "The directive linking function that configs the typeahead element"
  [template $rootScope $compile $timeout]
  (fn [scope element attrs] (let [value-key (.-lovValueKey attrs)
                                  lov-model (.-lovModel attrs)
                                  lov-disabled (.-lovDisabled attrs)
                                  update-model (fn [event datum name]
                                                 (set-in-scope scope lov-model (.-object datum))
                                                 (.$digest $rootScope))
                                  set-query-value (fn [value] 
                                                         (let [text-value (if (or (nil? value) (undefined? value)) 
                                                                            "" 
                                                                            (aget value value-key))]
                                                         (.typeahead element "setQuery" (if text-value text-value ""))))
                                  setup-typeahead (fn []
                                                    (let [dataset-is-valid-attr (.-lovDatasetIsValid attrs)
                                                          dataset-is-valid (if (nil? dataset-is-valid-attr) true
                                                                             (.$eval scope (str "(" dataset-is-valid-attr ")? true:false")))]
                                                      (.typeahead element "destroy")
                                                      (.off element)
                                                      (when-not dataset-is-valid (.log js/console "Data set is not valid"))
                                                      (when dataset-is-valid
                                                        (when-not (or (nil? lov-disabled) (undefined? lov-disabled))
                                                          (let [disabled (.$eval scope lov-disabled)]
                                                            (if disabled 
                                                              (.attr element "disabled" "disabled")
                                                              (.removeAttr element "disabled"))))
                                                        (.typeahead element 
                                                          (let [name (.-lovTypeahead attrs)
                                                                limit (.-lovLimit attrs)
                                                                local (.-lovLocal attrs)
                                                                prefetch (.-lovPrefetch attrs)
                                                                remote (.-lovRemote attrs)
                                                                options {:name name}
                                                                filter-fn #(d/json->dataset value-key %)
                                                                options (m/no-nil-assoc options :local local (filter-fn (.$eval scope local)))
                                                                options (m/no-nil-assoc options :prefetch prefetch {:url prefetch, :filter filter-fn})
                                                                options (m/no-nil-assoc options :remote remote {:url remote, :filter filter-fn})
                                                                options (m/no-nil-assoc options :limit limit)
                                                                options (m/no-nil-assoc options :template template (let [linkFn ($compile template)]
                                                                                                                     (fn [context] 
                                                                                                                       (let [scope (.$new $rootScope)
                                                                                                                             element (let [the-object (.-object context)
                                                                                                                                           properties (keys (js->clj the-object))]
                                                                                                                                       (doall (map #(aset scope % (aget the-object %)) properties))
                                                                                                                                       (linkFn scope))
                                                                                                                             html (let [dom-element (aget element 0)]
                                                                                                                                    (.$apply scope)
                                                                                                                                    (aget dom-element "outerHTML"))]
                                                                                                                         (.$destroy scope)
                                                                                                                         html))))
                                                                options-js (clj->js options)]
                                                            options-js))
                                                        (doto element
                                                          (.on "typeahead:selected" update-model)
                                                          (.on "typeahead:autocompleted" update-model)
                                                          (.on "destroy" (fn [] 
                                                                           (.typeahead element "setQuery" "")
                                                                           (.typeahead element "destroy")
                                                                           (.off element)))
                                                          (.on "typeahead:closed" (fn []
                                                                        ( if (and (.val element) (< 0 (count (.val element))))
                                                                          (do
                                                                            (set-query-value (.$eval scope lov-model)))
                                                                          (do 
                                                                            (set-in-scope scope lov-model js-obj)
                                                                            (.$digest scope))))))
                                                        (set-query-value (.$eval scope lov-model)))))]
                              (.$observe attrs "lovTypeahead" (fn [] ($timeout (fn []
                                                                                 (setup-typeahead)))))
                              (.$watch scope lov-model (fn [value] (set-query-value value)))
                              (when-not (or (nil? lov-disabled) (undefined? lov-disabled))
                                (.$watch scope lov-disabled (fn [disabled-value]
                                                              ($timeout (fn []
                                                                          (setup-typeahead)))))))))

(def lovTypeaheadModule
   "The definition of the lov-typeahead's module"
   (.module js/angular "lovTypeahead" (clj->js [])))

(.directive lovTypeaheadModule
  "lovTypeahead"
  (clj->js ["$rootScope", "$compile", "$timeout",
            (fn [$rootScope, $compile, $timeout]
              (clj->js {:compile (fn [tElement tAttrs]
                                   (let [template (.-lovTemplate tAttrs)]
                                     (when template (.$set tAttrs "lovTemplate" ""))
                                     (templated-link-typeahead template $rootScope $compile $timeout)))}))]))

