(ns osrs-crafting-lookup.pages.recipe
  (:require [rum.core :as rum]
            [ajax.core :refer [GET]]))

(rum/defc materials [materials]
  [:div {} [:h1 {} "Materials"]
   (map (fn [material]
          (identity [:div
                     [:h2 (str (:id (first material)) "-" (:recipe_id (first material)))]
                     (map (fn [inner] [:p (str inner)]) material)])) materials)])

(rum/defc target [target]
  [:div {} [:h1 {} "Target"] [:p {} (str target)]])

(rum/defc products [products]
  [:div {} [:h1 {} "Products"] (map #(identity [:div [:h2 (:id %1)] [:p (str %1)]]) products)])

(rum/defc recipe < rum/reactive [id contents]
  [:div {}
   (materials (map #(get %1 :materials) (get-in contents [:target :recipes])))
   (target (:target contents))
   (products (:products contents))])
