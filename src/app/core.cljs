(ns app.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as d]
   [reagent.dom.server :as s]
   [malli.core :as m]
   [malli.dev.cljs]
   [malli.dev.pretty :as pretty]))

(+ 5 6)

(defn is-5?
  {:malli/schema [:=> [:cat :int] :boolean]}
  [n]
  (= 5 n))

(is-5? 5)
(is-5? 10)

(def Hiccup [:vector :any])

(s/render-to-static-markup [:div "hello world"])
(s/render-to-static-markup [:div {:style {:color "red"}} "hello world"])

(defn list-to-hiccup
  "Converts a list of strings to an unordered list."
  {:malli/schema [:=> [:cat [:sequential :string]] Hiccup]}
  [strings]
  [:ul
   (for [item strings]
     [:li {:key item} item])])

(def data ["first" "second" "third"])

(list-to-hiccup data)

(def click-count (r/atom 0 :validator #(m/validate [:int] %)))

(defn counting-component []
  [:div
   "The atom " [:code "click-count"] " has value: "
   @click-count ". "
   [:input {:type "button" :value "Click me!"
            :on-click #(swap! click-count inc)}]])

(identity @click-count)

(defn home-page []
  (fn []
    [:div [:h2 "My App"]
     [:div "hello world!"]
     (list-to-hiccup data)
     (counting-component)]))


;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:dev/after-load refresh []
  (prn "Hot code Remount")
  (malli.dev.cljs/start! {:report (pretty/reporter)})
  (mount-root))

(defn ^:export init! []
  (malli.dev.cljs/start! {:report (pretty/reporter)})
  (mount-root))
