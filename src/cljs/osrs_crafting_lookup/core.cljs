(ns osrs-crafting-lookup.core
  (:import (goog.History))
  (:require [rum.core :as rum]
            [ajax.core :refer [GET]]
            [clojure.string :as string]
            [osrs-crafting-lookup.components.nav :refer [nav]]
            [osrs-crafting-lookup.pages.home :refer [home]]
            [osrs-crafting-lookup.pages.recipe :refer [recipe]]
            [osrs-crafting-lookup.pages.recipes :refer [recipes]]))

(enable-console-print!)

(defonce page-type (atom nil))

(defn get-search []
  (as-> (.. js/window -location -search) $
    (string/replace $ "?" "")
    (string/split $ "&")
    (map #(string/split %1 "=") $)))

(defn update-page-type [type arg contents]
  (let [current-search (get-search)
        search-match (filter (fn [key val] (and (= key type) (= val arg))) current-search)]
    (if (or (nil? search-match) (empty? search-match))
      (js/history.pushState [type arg contents] (. js/document -title) (str "?" type "=" arg)))
    (swap! page-type #(identity [type arg contents]))))

(defn fetch [url params handler-params]
  (js/console.log "Fetching results from:" url)
  (GET url {:params          params
            :response-format :json
            :keywords?       true
            :handler         #(apply update-page-type (conj handler-params %1))}))

(rum/defc render-page [slot]
  [:div {} (nav fetch) (slot)])

(defn get-page [[key val contents]]
  (case key
    "recipes" #(recipes val contents fetch)
    "recipe" #(recipe val contents)
    #(home)))

(defn initial-page-content-load-fn [[key val]]
   (case key
    "recipes" #(fetch "/api/recipes" {:name val} ["recipes" val])
    "recipe" #(fetch "/api/recipe" {:id val} ["recipe" val])
    #()))

(rum/defc app < rum/reactive []
  (if (nil? (rum/react page-type))
    (->> (get-search)
      (map initial-page-content-load-fn)
      (first)
      (#(%1))))
  (render-page (get-page (rum/react page-type))))

(defn render []
  (rum/mount (app) (. js/document (getElementById "app"))))
