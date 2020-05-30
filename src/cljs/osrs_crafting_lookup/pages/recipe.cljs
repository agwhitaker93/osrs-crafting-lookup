(ns osrs-crafting-lookup.pages.recipe
  (:require [rum.core :as rum]
            [ajax.core :refer [GET]]))

(rum/defc materials [mats]
  [:div {} [:h1 {} "Materials"] (map #(identity [:div [:h2 (get-in %1 [:target :id])] [:p (str %1)]]) mats)])

(rum/defc target [target]
  [:div {} [:h1 {} "Target"] [:p {} (str target)]])

(rum/defc products [products]
  [:div {} [:h1 {} "Products"] (map #(identity [:div [:h2 (get-in %1 [:target :id])] [:p (str %1)]]) products)])

(rum/defc recipe < rum/reactive [id contents]
  [:div {}
   (materials (:materials contents))
   (target (:target contents))
   (products (:products contents))])
