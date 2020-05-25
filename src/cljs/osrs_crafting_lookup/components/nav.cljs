(ns osrs-crafting-lookup.components.nav
  (:require [rum.core :as rum]))

(defonce search-term (atom ""))

(defn submit-search [handler]
  (let [search-term (deref search-term)
        page-title (. js/document -title)]
    (handler "recipes" search-term)
    (js/history.pushState {"recipes" search-term} page-title (str "?recipes=" search-term))))

(defn listen-enter [event cb]
  (if (= (.-key event) "Enter")
    (submit-search cb)))

(defn update-search-term [event]
  (swap! search-term #(str (-> event
                               .-target
                               .-value))))

(rum/defc home []
  [:ul {:class "nav-left"}
   [:li {:class "nav-left-contents"} [:a {:class "nav-left-link" :href "/"} "Home"]]])

(rum/defc search [search-result-cb]
  [:div {} [:input {:class       "nav-search"
                    :type        "text"
                    :placeholder "Search..."
                    :on-key-down #(listen-enter %1 search-result-cb)
                    :on-change   update-search-term}]
   [:button {:class    "nav-search-button"
             :on-click #(submit-search search-result-cb)} "Search"]])

(rum/defc nav [search-result-cb]
  [:div {:class "nav-bar"} (home)
   (search search-result-cb)])
