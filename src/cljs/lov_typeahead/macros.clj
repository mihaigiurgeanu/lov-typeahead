(ns lov-typeahead.macros)

(defmacro no-nill-assoc
  "Helper macro used to create the typeahead options map based on existence or not of optional attributes on the input text tag."
  ([options key value] 
    (if (nil? value) options
      `(assoc ~options ~key ~value)))
  ([options key test value] 
    (if (nil? test) options
      `(assoc ~options ~key ~value))))
