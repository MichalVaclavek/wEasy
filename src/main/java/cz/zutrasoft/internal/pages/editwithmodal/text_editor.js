/**
 * 
 */
 <script>
            // =================================================================
            // Object TextEditor
            // =================================================================
            function TextEditor(textEditId, textSendId, textViewId) {
                // Edit area (text area)
                var textEditElement = document.getElementById(textEditId);
                // Text area with correct tags (not the fake ones)
                var textSendElement = document.getElementById(textSendId);
                // View area (div)
                var textViewElement = document.getElementById(textViewId);

                var START_PREFIX = "[";
                var START_SUFFIX = "]";
                var END_PREFIX = "[/";
                var END_SUFFIX = "]";
                var IMG_SUFFIX = "/]";

				// Modifies textarea before submit. Replace [] for <> so the text could be saved to database.
				this.modifyTextArea = function() {
					textSendElement.value = this.replaceFakeTags();
				}
                
                // Inserts tag(s) into the edit area
                this.insertImage = function (tag) {
                    var imgTag = START_PREFIX + tag.toUpperCase() + " " + "src=''" + src + "''" + IMG_SUFFIX;
                    var text = textEditElement.value;
                    var begin = text.substring(0, textEditElement.selectionStart);
                    var end = text.substring(textEditElement.selectionEnd);
                    textEditElement.value = begin + imgTag + end;
                };

             	// Inserts img with src into the edit area
                this.modifySelection = function (tag, scr) {
                    var startTag = START_PREFIX + tag.toUpperCase() + START_SUFFIX;
                    var endTag = END_PREFIX + tag.toUpperCase() + END_SUFFIX;
                    var text = textEditElement.value;
                    var begin = text.substring(0, textEditElement.selectionStart);
                    var selection = text.substring(textEditElement.selectionStart, textEditElement.selectionEnd);
                    var end = text.substring(textEditElement.selectionEnd);
                    textEditElement.value = begin + startTag + selection + endTag + end;
                };

                // Modifies editing area. It is called when article is loaded from database to replace <>.
                this.modifyArea = function() {
                	textEditElement.value = this.replaceTagsForFakes(); 
                }

                // Replaces tag in edit area for relevant html tags and shows the text in view area
                this.viewText = function () {
                    var text = this.replaceFakeTags();
                    textViewElement.innerHTML = text;
                    showPreviewModalWindow();
                };

                // Replaces tags for the fake tags.
                this.replaceTagsForFakes = function() {
					var text = textEditElement.value;

					// Bold
                    text = text.replace(/<b>/gi, "[b]");
                    text = text.replace(/<\/b>/gi, "[/b]");
                    // Italic
                    text = text.replace(/<i>/gi, "[i]");
                    text = text.replace(/<\/i>/gi, "[/i]");
                    // Underline
                    text = text.replace(/<u>/gi, "[u]");
                    text = text.replace(/<\/u>/gi, "[/u]");

                    // Headers
                    text = text.replace(/<h1>/gi, "[h1]");
                    text = text.replace(/<\/h1>/gi, "[/h1]");
                    text = text.replace(/<h2>/gi, "[h2]");
                    text = text.replace(/<\/h2>/gi, "[/h2]");
                    text = text.replace(/<h3>/gi, "[h3]");
                    text = text.replace(/<\/h3>/gi, "[/h3]");
                    text = text.replace(/<h4>/gi, "[h4]");
                    text = text.replace(/<\/h4>/gi, "[/h4]");

                    // A, PRE, CODE, P
                    text = text.replace(/<a/gi, "[a");
                    text = text.replace(/">/gi, "\x22a]");
                    text = text.replace(/<\/a>/gi, "[/a]");
                    text = text.replace(/<pre>/gi, "[pre]");
                    text = text.replace(/<\/pre>/gi, "[/pre]");
                    text = text.replace(/<code>/gi, "[code]");
                    text = text.replace(/<\/code>/gi, "[/code]");
                    text = text.replace(/<p>/gi, "[p]");
                    text = text.replace(/<\/p>/gi, "[/p]");

//                     text = text.replace(/<br\/>/gi, "\n");

                    text = text.replace(/<img/gi, "[img");
                    text = text.replace(/\/>/gi, "/img]");

                    

                    return text;
                };

                // Replaces fake tags for the real ones.
                this.replaceFakeTags = function() {
                	var text = textEditElement.value;

                    // Replace all < and > chars.
                    text = text.replace(/</gi, "&lt;");
                    text = text.replace(/>/gi, "&gt;");

                    // Bold
                    text = text.replace(/\[b\]/gi, "<b>");
                    text = text.replace(/\[\/b\]/gi, "</b>");
                    // Italic
                    text = text.replace(/\[i\]/gi, "<i>");
                    text = text.replace(/\[\/i\]/gi, "</i>");
                    // Underline
                    text = text.replace(/\[u\]/gi, "<u>");
                    text = text.replace(/\[\/u\]/gi, "</u>");

                    // Headers
                    text = text.replace(/\[h1\]/gi, "<h1>");
                    text = text.replace(/\[\/h1\]/gi, "</h1>");
                    text = text.replace(/\[h2\]/gi, "<h2>");
                    text = text.replace(/\[\/h2\]/gi, "</h2>");
                    text = text.replace(/\[h3\]/gi, "<h3>");
                    text = text.replace(/\[\/h3\]/gi, "</h3>");
                    text = text.replace(/\[h4\]/gi, "<h4>");
                    text = text.replace(/\[\/h4\]/gi, "</h4>");

                    // A, PRE, CODE, P
                    text = text.replace(/\[a/gi, "<a");
                    text = text.replace(/"a\]/gi, "\x22>");
                    text = text.replace(/\[\/a\]/gi, "</a>");
                    text = text.replace(/\[pre\]/gi, "<pre>");
                    text = text.replace(/\[\/pre\]/gi, "</pre>");
                    text = text.replace(/\[code\]/gi, "<code>");
                    text = text.replace(/\[\/code\]/gi, "</code>");
                    text = text.replace(/\[p\]/gi, "<p>");
                    text = text.replace(/\[\/p\]/gi, "</p>");


                    text = text.replace(/\[img/gi, "<img");
                    text = text.replace(/\/img\]/gi, "/>");

//                     text = text.replace(/\n/gi, "<br/>");

                    return text;
                }
                
                // Inserts img tags with src attribute
                this.insertImage = function(url) {
                    var text = textEditElement.value;
                    var position = textEditElement.selectionStart;
                    var before = text.substring(0, position);
                    var after = text.substring(position);
                    var tag = START_PREFIX + "IMG SRC='" + url + "'/IMG" + START_SUFFIX; 
                    textEditElement.value = before + tag + after;
                };
                
                // Clears the edit area
                this.clearEditArea = function() {
                    textEditElement.value = "";
                };
            }
            
            // =================================================================
            // Object one directory with images array
            // =================================================================
            function DirImages() {
                var directory;
                var images = [];

                this.setDirectory = function (dir) {
                    directory = dir;
                };
                this.getDirectory = function () {
                    return directory;
                };
                this.addImage = function (image) {
                    images[images.length] = image;
                };
                this.getImages = function () {
                    return images;
                };
            }

            // Declaration and instantination of objects
            var textEditor = new TextEditor("textEdit", "textSend", "textView");

            function showPreviewModalWindow() {
                var modal = document.getElementById("previewModalWindow");
				modal.style.display="block";
            }
            function closePreviewModalWindow() {
            	var modal = document.getElementById("previewModalWindow");
				modal.style.display="none";
            }
            
</script>