(ns osrs-crafting-lookup.components.item-card
  (:require [rum.core :as rum]))

(rum/defc header [contents]
  [:div {:class "item-card-header"
         :on-click (:on-click contents)}
   [:img {:class "item-card-header-icon"
          :src (:img contents)}]
   [:div {:class "item-card-header-title"} (:title contents)]
   [:a {:class "item-card-header-wiki-link"
        :href (:wiki-link contents)} "W"]])

(rum/defc body [contents]
  [:div {:class "item-card-body"} (str contents)])

(rum/defc footer [contents]
  [:div {:class "item-card-footer"}
   [:div {:class "item-card-footer-left"} (:left contents)]
   [:div {:class "item-card-footer-right"} (:right contents)]])

(rum/defc card [contents]
  [:div {:class "item-card"
         :id (:id contents)}
   (header (:header contents))
   (body (:body contents))
   (footer (:footer contents))])
