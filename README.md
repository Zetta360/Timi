<h1 id="toc_0">OpenAI Mobile Environment Interaction</h1>

<p>This project was created during a 12-hour hackathon to investigate how OpenAI behaves when interacting with the mobile environment. The goal was to measure reaction times and data consistency, as well as to explore how to build a prompt for the Engine offered by the DaVinci model.</p>

<h2 id="toc_1">Technology Used</h2>

<p>For this project, we used Java for the OpenAI API, as the Kotlin library was found to be unstable. Additionally, we used Android Studio as our IDE, and we used a mobile device running Android as our testing platform.</p>

<h2 id="toc_2">Results</h2>

<p>During our testing, we found that the OpenAI API was generally quite responsive, with minimal delay when sending requests and receiving responses. However, we did notice some inconsistency in the data, particularly when working with longer prompts or when trying to generate more complex outputs. We also noted that the token consumption ratio varied widely depending on the type of prompt and the length of the output.</p>

<h2 id="toc_3">Challenges</h2>

<p>Some of the challenges we faced during this project included <strong>dealing with the penalty of tokens via prompt delivery</strong>, <strong>ensuring data consistency</strong>, and <strong>optimizing the</strong> <strong>token consumption ratio</strong>. We also found that the AI performance on mobile devices was not always as strong as we had hoped, which could limit the types of applications that could be built using this technology.</p>

<h2 id="toc_4">Future Work</h2>

<p>Moving forward, we believe that there is still a great deal of potential in using OpenAI to interact with mobile devices. Some areas that we would like to explore further include optimizing the prompt delivery mechanism to reduce token penalties, developing strategies for ensuring data consistency, and improving the overall AI performance on mobile devices. Additionally, we plan to experiment with different types of prompts and outputs to better understand the token consumption ratio and how it can be managed.</p>

<h2 id="toc_5">Conclusion</h2>

<p>Overall, we found this project to be a valuable exploration of how OpenAI can be used in mobile environments. Although there were certainly challenges and limitations to our work, <strong>we believe that the potential benefits of this technology make it well worth further investigation and development.</strong> </p>
