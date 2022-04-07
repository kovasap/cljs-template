(ns app.schemas-and-generation
   [malli.core :as m]
   [malli.generator :refer [generate]])

; Make better once https://github.com/metosin/malli/issues/687 is fixed.
(def Hiccup [:vector :any])

(generate (m/schema float?))
(generate (m/schema Hiccup))


(defn list-to-hiccup
  "Converts a list of string to hiccup."
  {:malli/schema [:=> [:cat [:sequential :string]] Hiccup]}
  [list]
  [:ul
   (for [item list]
     [:li {:key item} item])])

(:malli/schema (meta #'list-to-hiccup))
