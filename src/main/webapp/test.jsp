<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <title>KopiDoc</title>
    <script type="text/javascript">
      var config = {
        contextPath: '${pageContext.request.contextPath}'
      };
    </script>
    <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/yui/2.8.1/build/reset-fonts-grids/reset-fonts-grids.css" />
    <link rel="stylesheet/less" href="css/style.less" type="text/css" />
    <script type="text/javascript" src="css/vendor/less-1.0.35.min.js"></script>
    <script type="text/javascript">
      less.env = "development";
      less.watch();
    </script>
  </head>
  <body>
    <nav id="menu">
      <ol id="packageList">
        <li>
          <h2>com.etsy.solr</h2>
          <ul>
            <li><a rel="type" href="#">SolrConfigThing</a></li>
            <li><a rel="type" href="#">SolrConfigThing</a></li>
            <li><a rel="type" href="#">SolrConfigThing</a></li>
          </ul>
        </li>
        <li>
          <h2>com.etsy.person</h2>
          <ul>
            <li><a rel="type" href="#">SolrConfigThing</a></li>
            <li><a rel="type" href="#">SolrConfigThing</a></li>
            <li><a rel="type" href="#">SolrConfigThing</a></li>
          </ul>
        </li>
      </ol>
      <dl id="instructions">
        <dt>/<dt>
        <dd>Search</dd>
        <dt>?</dt>
        <dd>Config</dd>
      </dl>
      <div id="searchExplore">
        <input type="text" />
      </div>
    </nav>
    <article>
      <header>
        <h1>SolrConfigListenerFactory</h1>
        <span class="packageName">com.etsy.solr</span>
        <span title="final" class="final" />
        <span title="abstract" class="abstract" />
        <span title="static" class="static" />
      </header>
      <nav>
        <ul>
          <li><a href="#constructors">Constructors</a></li>
          <li><a href="#methods">Methods</a></li>
          <li><a href="#fields">Fields</a></li>
        </ul>
      </nav>
      <div id="classLocation">
        <div class="inheritance">
          <label for="inheritanceChain">Inheritance</label>
          <ol id="inheritanceChain" class="chain">
            <li><a rel="type" href="#">Object</a></li>
            <li><a rel="type" href="#">AbstractReader</a></li>
            <li><a rel="type" href="#">Reader</a></li>
          </ol>
        </div>
        <div class="implements">
          <label for="implementsList">Implements</label>
          <ul id="implementsList" class="list">
            <li><a rel="type" href="#">Printable</a></li>
            <li><a rel="type" href="#">SocketSucker</a></li>
          </ul>
        </div>
        <div class="subClasses">
          <label for="subClassList">Subclasses</label>
          <ul class="list">
            <li><a rel="type" href="#">EtsyConfigLoader</a></li>
          </ul>
        </div>
      </div>
      <details open="open" class="commentBlock">
        A SolrConfigThing allows XML to interact with a VisualBasic GUI for locating an IP
      </details>

      <section>
        <header>
          <h2>Constructors</h2>
          <ul class="filters">
            <li>
              <select>
                <option>All Scopes</option>
                <option>Public</option>
                <option>Private</option>
                <option>Protected</option>
              </select>
            </li>
            <li>
              <select>
                <option>All Parameters</option>
                <option>String</option>
                <option>Location</option>
              </select>
            </li>
            <li>
              <button>Show Inherited</button>
            </li>
            <li>
              <input type="checkbox" class="abstract" />
              <input type="checkbox" class="static" />
              <input type="checkbox" class="final" />
            </li>
          </ul>
        </header>
        <ol>
          <li>
            <header class="public">
              <ol class="parameterList">
                <li><a rel="type" class="type" href="#">Double</a></li>
                <li><a rel="type" class="type" href="#">Double</a></li>
                <li><a rel="type" class="type" href="#">Double</a></li>
              </ol>
              <span class="describedBy"></span>
              Return a new thing based on two doubles.  Return a new thing based on two doubles. Return a new thing based on two doubles.
              <span class="final" />
            </header>
            <ul class="throwList">
              <li><a rel="type" class="type" href="#">Exception</a></li>
              <li><a rel="type" class="type" href="#">UnknownLocationException</a></li>
            </ul>
            <div class="extraInfo">
              <details class="commentBlock">
                Return a new thing based on two doubles.  Return a new thing based on two doubles. Return a new thing based on two doubles.
              </details>
              <section>
                <h5>Parameters</h5>
                <ol>
                  <li>
                    <a rel="type" href="#" class="type">Double</a>
                    <var>latitude</var>
                    <span class="describedBy"></span>
                    <details class="commentSummary">
                      The latitude of the thing to be configured
                    </details>
                  </li>
                  <li>
                    <a rel="type" href="#" class="type">Double</a>
                    <var>longitude</var>
                    <span class="describedBy"></span>
                    <details class="commentSummary">
                       The longitude of the thing that we care about
                    </details>
                  </li>
               </ol>
              </section>
              <section>
                <h5>Returns</h5>
                <a rel="type" href="#" class="type">Location</a>
                <span class="describedBy"></span>
                <details class="commentSummary">
                  The Location of the thing for which a latitude and longitude are defined
                </details>
              </section>
              <section>
                <h5>Throws</h5>
                <ul>
                  <li>
                    <a rel="type" href="#" class="type">UnknownLocationException</a>
                    <span class="describedBy"></span>
                    <details class="commentSummary">
                      if the location cannot be found
                    </details>
                  </li>
                </ul>
              </section>
            </div> <!-- .extraInfo -->
          </li>
        </ol>
      </section>

      <section>
        <header>
          <h2>Methods</h2>
          <ul class="filters">
            <li>
              <select>
                <option>All Scopes</option>
                <option>Public</option>
                <option>Private</option>
                <option>Protected</option>
              </select>
            </li>
            <li>
              <select>
                <option>All Parameters</option>
                <option>String</option>
                <option>Location</option>
              </select>
            </li>
            <li>
              <button>Show Inherited</button>
            </li>
            <li>
              <input type="checkbox" class="abstract" />
              <input type="checkbox" class="static" />
              <input type="checkbox" class="final" />
            </li>
          </ul>
        </header>
        <ol>
          <li>
            <header class="private">
              <label>getLocationFromLatLon</label>
              <span class="describedBy"></span>
              Return a new thing based on two doubles.  Return a new thing based on two doubles. Return a new thing based on two doubles.
              <span class="final" />
            </header>
            <div class="methodSignature">
              <a rel="type" href="#">Location</a>
              <span class="returnedBy"></span>
              <ol class="parameterList">
                <li><a rel="type" class="type" href="#">Double</a></li>
                <li><a rel="type" class="type" href="#">Double</a></li>
              </ol>
            </div>
            <ul class="throwList">
              <li><a rel="type" class="type" href="#">Exception</a></li>
              <li><a rel="type" class="type" href="#">UnknownLocationException</a></li>
            </ul>
            <div class="extraInfo">
              <details class="commentBlock">
                Return a new thing based on two doubles.  Return a new thing based on two doubles. Return a new thing based on two doubles.
              </details>
              <section>
                <h5>Parameters</h5>
                <ol>
                  <li>
                    <a rel="type" href="#" class="type">Double</a>
                    <var>latitude</var>
                    <span class="describedBy"></span>
                    <details class="commentSummary">
                      The latitude of the thing to be configured
                    </details>
                  </li>
                  <li>
                    <a rel="type" href="#" class="type">Double</a>
                    <var>longitude</var>
                    <span class="describedBy"></span>
                    <details class="commentSummary">
                       The longitude of the thing that we care about
                    </details>
                  </li>
               </ol>
              </section>
              <section>
                <h5>Returns</h5>
                <a rel="type" href="#" class="type">Location</a>
                <span class="describedBy"></span>
                <details class="commentSummary">
                  The Location of the thing for which a latitude and longitude are defined
                </details>
              </section>
              <section>
                <h5>Throws</h5>
                <ul>
                  <li>
                    <a rel="type" href="#" class="type">UnknownLocationException</a>
                    <span class="describedBy"></span>
                    <details class="commentSummary">
                      if the location cannot be found
                    </details>
                  </li>
                </ul>
              </div> <!-- .extraInfo -->
            </li>
          </ol>
        </section>

      <section>
        <header>
          <h2>Fields</h2>
          <ul class="filters">
            <li>
              <select>
                <option>All Scopes</option>
                <option>Public</option>
                <option>Private</option>
                <option>Protected</option>
              </select>
            </li>
            <li>
              <select>
                <option>All Types</option>
                <option>String</option>
                <option>Location</option>
              </select>
            </li>
            <li>
              <button>Show Inherited</button>
            </li>
            <li>
              <input type="checkbox" class="abstract" />
              <input type="checkbox" class="static" />
              <input type="checkbox" class="final" />
            </li>
          </ul>
        </header>
        <ol>
          <li>
            <header class="protected">
              <label>locationSet</label>
              <span><a rel="type">Map</a>&lt;<a rel="type" href="#">String</a>,<a rel="type" href="#">Location</a>&gt;</span>
              Return a new thing based on two doubles.  Return a new thing based on two doubles. Return a new thing based on two doubles.
              <span class="final" />
            </header>
          </li>
        </ol>
      </section>

      </article>
      <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.json-2.2.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/org/cometd.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.cometd.js"></script>
      <script type="text/javascript" src="connect.js"></script>
    </body>
  </html>
