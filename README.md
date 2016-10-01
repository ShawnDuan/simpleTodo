# Pre-work - *SimpleTodo*

**SimpleTodo** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Shawn Duan**

Time spent: **15** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can **successfully add and remove items** from the todo list
* [X] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [X] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [X] Persist the todo items ~~[into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite)~~ [into Realm](https://realm.io/docs/java/latest/) instead of a text file
* [X] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [ ] Add support for completion due dates for todo items (and display within listview item)
* [X] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [ ] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [X] Use **Realm** database to persist todo items.
* [X] Replace ListView with RecyclerView.
* [X] Add support for selecting estimated time needed for this task.
* [X] Floating action button for adding new todo item.
* [X] Reuse the same DialogFragment for both creating and editing item.
* [X] Add field to indicate if an item is done.

* [X] **Support for automatically picking up something in the time range to do, by given the free time you have now. **
* [X] **ArchiveActivity for displaying finished items**
* [X] When long click on an item, show a dialog for choosing "Delete" or "Complete" this thing.

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<div>
    <img src='https://raw.githubusercontent.com/shawnduan/simpleTodo/master/art/todolist_demo.gif' style='border: #f1f1f1 solid 1px'/>
</div>

## Notes

Describe any challenges encountered while building the app.

## License

    Copyright [2016] [Shawn Duan]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
