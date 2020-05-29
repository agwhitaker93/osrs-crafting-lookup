(ns osrs-crafting-lookup.styles
  (:require [garden-watcher.def :refer [defstyles]]))

(def primary-color "purple")
(def secondary-color "#aaa")
(def tertiary-color "#ccc")
(def quaternary-color "#eee")

(defstyles style
  [:body {:margin 0}]
  [:.flex-container {:display         "flex"
                     :flex-wrap       "wrap"
                     :justify-content "center"}]

  ; NAV
  [:.nav-bar {:background-color primary-color
              :overflow         "hidden"}
   [:.nav-links {:float           "left"
                 :list-style-type "none"
                 :margin          "0"
                 :padding         "0"}]
   [:.nav-link {}]
   [:.nav-link-anchor {:background-color quaternary-color
                       :display          "block"
                       :padding          "0.8rem 1rem"}
    [:&:hover {:background-color tertiary-color}]]
   [:.nav-search {:float        "right"
                  :margin-top   "0.3rem"
                  :margin-right "0.3rem"}]
   [:.nav-search-input {:border  "none"
                        :padding "0.6rem"}]
   [:.nav-search-button {:display          "inline-block"
                         :border           "none"
                         :padding          "0.6rem"
                         :cursor           "pointer"
                         :background-color quaternary-color}
    [:&:hover {:background-color tertiary-color}]]]

  ; RESULTS
  [:.item-card {:box-shadow "0 4px 8px 0 rgba(0,0,0,0.2)"
                :transition "0.3s"
                :width      "300px"
                :height     "100%"
                :margin     "10px"}
   [:.item-card-header {}
    [:.item-card-header-icon {:float            "left"
                              :padding          "4px"
                              :background-color quaternary-color
                              :width            "32px"
                              :height           "32px"}]
    [:.item-card-header-title {:display          "inline-block"
                               :padding          "11px 0"
                               :background-color secondary-color
                               :text-align       "center"
                               :width            "220px"}]
    [:.item-card-header-wiki-link {:float "right"
                                   :background-color primary-color
                                   :width "40px"
                                   :text-align "center"
                                   :padding "11px 0"}
     [:&:hover {:background-color "green"}]]]
   [:.item-card-body {:background-color tertiary-color
                      :height           "70px"
                      :padding          "5px"}]
   [:.item-card-footer {:background-color quaternary-color}
    [:.item-card-footer-left {:display "inline-block"
                              :padding "5px"}]
    [:.item-card-footer-right {:float   "right"
                               :padding "5px"}]]])
