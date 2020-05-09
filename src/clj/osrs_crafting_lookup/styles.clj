(ns osrs-crafting-lookup.styles
  (:require [garden-watcher.def :refer [defstyles]]))

(def el-height "2rem")
(def side-dist "0.5rem")
(def nav-height "3rem")
(def nav-button-width "4rem")

(defstyles style
           [:body {:padding-top nav-height}]
           [:.nav-bar {:position         "absolute"
                       :top              0
                       :left             0
                       :height           nav-height
                       :width            "100%"
                       :background-color "#85506e"}]
           [:.nav-inner {:position  "absolute"
                         :top       "50%"
                         :transform "translateY(-50%)"}]
           [:.nav-home {:left        side-dist
                        :height      el-height
                        :line-height el-height
                        :width       "4rem"
                        :text-align  "center"}]
           [:.nav-search {:right  (str "calc(" side-dist " + " nav-button-width ")")
                          :height el-height}]
           [:.nav-search-button {:right  side-dist
                                 :height el-height
                                 :width  nav-button-width}])
