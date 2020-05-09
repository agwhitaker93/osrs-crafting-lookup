(ns osrs-crafting-lookup.components.results
  (:require [rum.core :as rum]))

(rum/defc item [contents]
  [:div {:class "results-item"} (str (:name contents))])

(rum/defc results < rum/reactive [result]
  (if (empty? (rum/react result))
    [:div {:class "results"} "Enter a search above"]
    (let [parsed (.parse js/JSON (rum/react result))
          parsed-clj (js->clj parsed :keywordize-keys true)]
      [:div {:class "results"} (map item parsed-clj)])))
