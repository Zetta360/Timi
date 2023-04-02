package com.cicada.sisi.manager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cicada.sisi.R

/**
 * Replace the fragment
 *
 */
fun moveToNext(newFragment : Fragment,
               fragmentManager : FragmentManager, container : Int = R.id.mainContainer) {

    fragmentManager.beginTransaction().replace(container, newFragment).commit()
}

/**
 * Remove a fragment from fragManager
 *
 */
fun remove(fragment : Fragment,
           fragmentManager : FragmentManager
){

    fragmentManager.beginTransaction().remove(fragment).commit()
}

/**
 * Open a new fragment on the last fragment
 *
 */
fun open(newFragment : Fragment,
         fragmentManager : FragmentManager, container : Int = R.id.mainContainer) {

    fragmentManager.beginTransaction().add(container, newFragment).commit()
}