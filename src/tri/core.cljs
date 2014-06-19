(ns tri.core
  (:require-macros
   [schema.macros :as sm]
   [dommy.macros :refer [node sel1]])
  (:require
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


(defcomponentk triangles
  []
  (render [this]
    (dom/div
     (str (triangulate [[0 0] [0 1] [1 1] [1 0]])))))

(def app-state
  {:points []})

(defn ^:export init []
  (let [container (node :#container)]
    (dommy/append! js/document.body container)
    (om/root triangles app-state {:target container})))

(init)
