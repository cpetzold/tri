(ns tri.core
  (:require-macros
   [schema.macros :as sm])
  (:require
   [om.core :as om :include-macros true]
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
