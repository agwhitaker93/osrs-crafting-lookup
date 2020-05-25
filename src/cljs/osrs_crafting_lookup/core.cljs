(ns osrs-crafting-lookup.core
  (:require [rum.core :as rum]
            [clojure.string :as string]
            [osrs-crafting-lookup.components.nav :refer [nav]]
            [osrs-crafting-lookup.pages.home :refer [home]]
            [osrs-crafting-lookup.pages.recipe :refer [recipe]]
            [osrs-crafting-lookup.pages.recipes :refer [recipes]]))

(enable-console-print!)

(defonce page-type (atom nil))

(defn update-page-type [type args]
  (swap! page-type #(identity [type args])))

(rum/defc render-page [slot]
  [:div {} (nav update-page-type) slot])

(defn get-page [[key val]]
  (case key
    "recipe" (render-page (recipe val))
    "recipes" (render-page (recipes update-page-type val))
    (render-page (home))))

(defn get-search []
  (as-> (.. js/window -location -search) $
        (string/replace $ "?" "")
        (string/split $ "&")
        (map #(string/split %1 "=") $)))

(rum/defc app < rum/reactive []
  (let [page-search (rum/react page-type)]
    (if (nil? page-search)
      (->> (get-search)
           (map get-page)
           (first))
      (get-page page-search))))

(defn render []
  (rum/mount (app) (. js/document (getElementById "app"))))
