(ns tri.core
  (:require-macros
   [schema.macros :as sm]
   [dommy.macros :refer [node sel1]])
  (:require
   [clojure.string :as str]
   [dommy.core :as dommy]
   [plumbing.core :as p :include-macros true]
   [om.core :as om :include-macros true]
   [om-tools.core :refer-macros [defcomponentk]]
   [om-tools.dom :as dom :include-macros true]
   [schema.core :as s]))

(sm/defschema Point
  (s/pair s/Num "x" s/Num "y"))

(sm/defschema Triangle
  [(s/one Point "a")
   (s/one Point "b")
   (s/one Point "c")])

(sm/defschema Color
  [(s/one s/Num "red")
   (s/one s/Num "green")
   (s/one s/Num "blue")])

(defn wrap-array-fn [f]
  (comp js->clj f clj->js))

(def triangulate*
  (wrap-array-fn js/Delaunay.triangulate))

(sm/defn triangulate :- [Triangle]
  [points :- [Point]]
  (->> points
       triangulate*
       (map (partial nth points))
       (partition 3)))

(sm/defn point-str :- s/Str
  [points :- [Point]]
  (->> points
       (map (partial str/join ","))
       (str/join " ")))

(sm/defn mouse-pos :- Point [e]
  [(.-clientX e) (.-clientY e)])

(defcomponentk triangles
  [[:data points mouse-point]]
  (render [this]
    (let [triangles (triangulate points)]
      (js/console.log (str mouse-point))
      (dom/svg
       {:on-mouse-move #(om/update! mouse-point (mouse-pos %))
        :on-mouse-down (fn [e]
                         (om/transact! points #(conj % (mouse-pos e))))}
       (for [triangle (-> points (p/conj-when mouse-point) triangulate)]
         (dom/polygon
          {:points (point-str triangle)
           :style {:stroke "white"
                   :stroke-width 1}}))))))

(def app-state
  {:points []
   :mouse-point []})

(defn ^:export init []
  (let [container (node :#container)]
    (dommy/append! js/document.body container)
    (om/root triangles app-state {:target container})))

(init)
