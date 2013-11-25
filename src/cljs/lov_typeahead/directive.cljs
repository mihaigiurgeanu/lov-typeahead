(ns lov-typeahead.directive
  (:require [lov-typeahead.dataset :as d])
  (:require-macros [lov-typeahead.macros :as m]))

(defn set-in-scope
  "Sets a value to an angular js model in a given scope"
  [scope model val] 
  (let [fields (clojure.string/split model #"\.")
        model-field (last fields)
        model-parent (do
                       (loop [crt scope
                              fields' fields]
                         (if (>= 1 (count fields'))
                           crt
                           (do
                             (let [this-field-name (first fields')
                                   safe-field (fn [val] 
                                                (if (or (nil? val) (undefined? val))
                                                  (let [newobj js-obj] 
                                                    (aset crt this-field-name newobj)
                                                    newobj)
                                                  val))]
                               (recur (safe-field (aget crt this-field-name))
                                      (rest fields')))))))]
    (aset model-parent model-field val)))

(defn templated-link-typeahead 
  "The directive linking function that configs the typeahead element"
  [template $rootScope $compile]
  (fn [scope element attrs] (let [value-key (.-lovValueKey attrs)
                                  lov-model (.-lovModel attrs)
                                  update-model (fn [event datum name]
                                                 (set-in-scope scope lov-model (.-object datum))
                                                 (.$digest scope))
                                  set-up-typeahead (fn []
                                                     (let [dataset-is-valid-attr (.-lovDatasetIsValid attrs)
                                                           dataset-is-valid (if (nil? dataset-is-valid-attr) true
                                                                              (.$eval scope (str "(" dataset-is-valid-attr ")? true:false")))]
                                                       (.typeahead element "destroy")
                                                       (when dataset-is-valid
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
                                                                                                                              element (do
                                                                                                                                        (aset scope "id" (.-id (.-object context)))
                                                                                                                                        (aset scope "name" (.-name (.-object context)))
                                                                                                                                        (linkFn scope))
                                                                                                                              html (do
                                                                                                                                     (.$apply scope)
                                                                                                                                     (.html element))]
                                                                                                                          (.$destroy scope)
                                                                                                                          html))))
                                                                 options-js (clj->js options)]
                                                             (.log js/console (str "options: " (.stringify js/JSON options-js)))
                                                             options-js)))))]
                              (.log js/console (str "the template: " template))
                              (.$observe attrs "lovTypeahead" set-up-typeahead)
                              (.$observe attrs "lovRemote" set-up-typeahead)
                              (.$watch scope lov-model (fn [value] (.val element (if (or (nil? value) (undefined? value)) 
                                                                                   "" 
                                                                                   (aget value value-key)))))
                              (doto element
                                (.on "typeahead:selected" update-model)
                                (.on "typeahead:autocompleted" update-model)))))

(def lovTypeaheadModule
   "The definition of the lov-typeahead's module"
   (.module js/angular "lovTypeahead" (clj->js [])))

(.directive lovTypeaheadModule
  "lovTypeahead"
  (clj->js ["$rootScope", "$compile",
            (fn [$rootScope, $compile]
              (clj->js {:compile (fn [tElement tAttrs]
                                   (let [template (.-lovTemplate tAttrs)]
                                     (when template (.$set tAttrs "lovTemplate" ""))
                                     (.log js/console "Creating the link function")
                                     (templated-link-typeahead template $rootScope $compile)))}))]))

