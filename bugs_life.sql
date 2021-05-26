-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 26, 2021 at 04:41 PM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 7.3.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bugs_life`
--

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `project_id` int(11) NOT NULL,
  `issue_id` int(11) NOT NULL,
  `comment_id` int(11) NOT NULL,
  `text` varchar(250) DEFAULT NULL,
  `comment_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `user` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`project_id`, `issue_id`, `comment_id`, `text`, `comment_timestamp`, `user`) VALUES
(1, 1, 1, 'Please find below links to the two .pbix files I\'ve created in an attempt toconquer this error I\'m receiving.', '2019-08-07 15:44:00', 'liew'),
(1, 1, 2, 'Try this https://www.dropbox.com/home?preview=Relationships.pbix\n\nJust I removed the relationship between sheet1 and sheet1BridgingTable, sheet2 and sheet2BrdgingTable', '2019-08-07 15:44:00', 'ang'),
(1, 2, 1, 'I am having the same issue with a disc of photos from Christmas given to me by my sister. I have tried opening all of the photos with all of my available opening options and still get the same message as above poster. ', '2019-08-08 15:44:00', 'liew'),
(1, 2, 2, 'lad photos of windows 8 & 10 are worst possible application ever developed!\n\n                    just change default view to \"windows photo viewer\" if you dont have any other application', '2019-08-08 15:44:00', 'btan'),
(1, 2, 3, 'Window 10 can but window vista does not work', '2019-08-08 15:44:00', 'ang'),
(2, 1, 1, 'Here is what I am referring to: https://streamable.com/hkyg3 Apologies for not posting a video when creating the issue! ', '2019-10-01 09:00:00', 'liew'),
(2, 1, 2, 'Thanks for video, seems interesting, weirdly enough I couldn\'t reproduce.\nWill see what can be done.', '2019-10-01 09:00:00', 'ang'),
(2, 1, 3, 'Thanks for everything', '2019-10-01 09:00:00', 'jhoe'),
(2, 2, 1, 'Still a problem. ', '2019-10-08 04:00:00', 'btan'),
(2, 2, 2, '/cc @clarkdo', '2019-10-08 04:00:00', 'ang'),
(3, 1, 1, 'To: https://streamable.com/hkyg3 Apologies for not posting a video when creating the issue!', '2019-09-01 12:00:00', 'liew'),
(3, 1, 2, 'Thanks for video, seems interesting, weirdly enough I couldn\'t reproduce.\nWill see what can be done.', '2019-09-01 12:00:00', 'ang');

-- --------------------------------------------------------

--
-- Table structure for table `comments_history`
--

CREATE TABLE `comments_history` (
  `project_id` int(11) NOT NULL,
  `issue_id` int(11) NOT NULL,
  `comment_id` int(11) NOT NULL,
  `version_id` int(11) NOT NULL,
  `text` varchar(250) DEFAULT NULL,
  `comment_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `user` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `issues`
--

CREATE TABLE `issues` (
  `project_id` int(11) NOT NULL,
  `issue_id` int(11) NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `tag` varchar(20) DEFAULT NULL,
  `descriptionText` varchar(500) DEFAULT NULL,
  `createdBy` varchar(20) DEFAULT NULL,
  `assignee` varchar(20) DEFAULT NULL,
  `issue_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `url` varchar(2083) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `issues`
--

INSERT INTO `issues` (`project_id`, `issue_id`, `title`, `priority`, `status`, `tag`, `descriptionText`, `createdBy`, `assignee`, `issue_timestamp`, `url`) VALUES
(1, 1, 'Can\'t display the table', 4, 'In Progress', 'Frontend', 'Hi, \n\n            I\'m receiving the \"Can\'t display the data because Power BI can\'t determine the relationship between two or more fields.\" error message and I do not understand why. I have searched the net for help but can\'t find a post that answers my question.\n\n           It may well be a straight modelling restriction but best to check.\n\n\n   I have two datasources (Sheet 1 and Sheet 2). Both are Excel files.', 'jhoe', 'btan', '2019-08-07 15:44:00', NULL),
(1, 2, 'Can\'t open file', 3, 'Open', 'Backend', 'Windows 10 Photo will not open a jpg file. I get \"We can\'t open this file.\" error message.\n\nThey\'re family photo that I sure would like to salvage.  Thanks', 'btan', 'jhoe', '2019-08-08 15:44:00', NULL),
(2, 1, 'Flash of unstyled content', 6, 'Open', 'cmty:bug-report', 'What is expected ?\nExpected correct scrollspy active link highlighting and all links work.\n\nWhat is actually happening?\nPage jumps to top before smooth scroll, which leads to weird links highlighting. Also, sometimes some links doesn\'t highlight (like Server Rendered in demo video)\n\nAdditional comments?\nDemonstration - https://imgur.com/a/GGvITVB', 'btan', 'ang', '2019-10-01 09:00:00', NULL),
(2, 2, 'User is \"trapped\" on 404 page', 8, 'Closed', 'Backend', 'When hitting the 404 page, the Nuxt layout isn\'t applied correctly.', 'ang', 'jhoe', '2019-10-08 04:00:00', NULL),
(3, 1, 'Flash of unstyled content', 1, 'Open', 'cmty:bug-report', 'What is expected ?\nThere is no flash of unstyled content            \n\nWhat is actually happening?\nThere is a brief flash of unstyled content\n\nAdditional comments? \nDoesn\'t occur in Chrome', 'liew', 'btan', '2019-09-01 12:00:00', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `issues_history`
--

CREATE TABLE `issues_history` (
  `project_id` int(11) NOT NULL,
  `issue_id` int(11) NOT NULL,
  `version_id` int(11) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `tag` varchar(20) DEFAULT NULL,
  `descriptionText` varchar(500) DEFAULT NULL,
  `createdBy` varchar(20) DEFAULT NULL,
  `assignee` varchar(20) DEFAULT NULL,
  `issue_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `projects`
--

CREATE TABLE `projects` (
  `project_id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `project_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `projects`
--

INSERT INTO `projects` (`project_id`, `name`, `project_timestamp`) VALUES
(1, 'Nuxtjs', '2019-08-07 15:44:00'),
(2, 'Vuejs', '2019-08-07 15:44:00'),
(3, 'Reactjs', '2019-08-07 15:44:00');

-- --------------------------------------------------------

--
-- Table structure for table `projects_history`
--

CREATE TABLE `projects_history` (
  `project_id` int(11) NOT NULL,
  `version_id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `originalTime` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `react`
--

CREATE TABLE `react` (
  `project_id` int(11) NOT NULL,
  `issue_id` int(11) NOT NULL,
  `comment_id` int(11) NOT NULL,
  `reaction` varchar(10) NOT NULL,
  `count` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `react`
--

INSERT INTO `react` (`project_id`, `issue_id`, `comment_id`, `reaction`, `count`) VALUES
(1, 1, 1, 'angry', 0),
(1, 1, 1, 'happy', 1),
(1, 1, 2, 'angry', 0),
(1, 1, 2, 'happy', 0),
(1, 2, 1, 'angry', 1),
(1, 2, 1, 'happy', 2),
(1, 2, 2, 'angry', 5),
(1, 2, 2, 'happy', 2),
(1, 2, 3, 'angry', 9),
(1, 2, 3, 'happy', 0),
(2, 1, 1, 'angry', 1),
(2, 1, 1, 'happy', 2),
(2, 1, 2, 'angry', 5),
(2, 1, 2, 'happy', 2),
(2, 1, 3, 'angry', 1),
(2, 1, 3, 'happy', 2),
(2, 2, 1, 'angry', 6),
(2, 2, 1, 'happy', 2),
(2, 2, 2, 'angry', 5),
(2, 2, 2, 'happy', 3),
(3, 1, 1, 'angry', 1),
(3, 1, 1, 'happy', 2),
(3, 1, 1, 'thumbsUp', 5),
(3, 1, 2, 'angry', 9),
(3, 1, 2, 'happy', 2);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userid` int(11) DEFAULT NULL,
  `username` varchar(25) DEFAULT NULL,
  `password` varchar(25) DEFAULT NULL,
  `admin` tinyint(1) DEFAULT NULL,
  `url` varchar(2083) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userid`, `username`, `password`, `admin`, `url`) VALUES
(1, 'jhoe', 'password', 0, NULL),
(2, 'btan', 'password', 0, NULL),
(3, 'liew', 'password', 0, NULL),
(4, 'ang', 'password', 0, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`project_id`,`issue_id`,`comment_id`);

--
-- Indexes for table `comments_history`
--
ALTER TABLE `comments_history`
  ADD PRIMARY KEY (`project_id`,`issue_id`,`comment_id`,`comment_timestamp`);

--
-- Indexes for table `issues`
--
ALTER TABLE `issues`
  ADD PRIMARY KEY (`project_id`,`issue_id`);

--
-- Indexes for table `issues_history`
--
ALTER TABLE `issues_history`
  ADD PRIMARY KEY (`project_id`,`issue_id`,`issue_timestamp`);

--
-- Indexes for table `projects`
--
ALTER TABLE `projects`
  ADD PRIMARY KEY (`project_id`);

--
-- Indexes for table `projects_history`
--
ALTER TABLE `projects_history`
  ADD PRIMARY KEY (`project_id`,`originalTime`);

--
-- Indexes for table `react`
--
ALTER TABLE `react`
  ADD PRIMARY KEY (`project_id`,`issue_id`,`comment_id`,`reaction`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD UNIQUE KEY `userid` (`userid`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `projects`
--
ALTER TABLE `projects`
  MODIFY `project_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comments_history`
--
ALTER TABLE `comments_history`
  ADD CONSTRAINT `pic_fk` FOREIGN KEY (`project_id`,`issue_id`,`comment_id`) REFERENCES `comments` (`project_id`, `issue_id`, `comment_id`);

--
-- Constraints for table `issues_history`
--
ALTER TABLE `issues_history`
  ADD CONSTRAINT `pi_fk` FOREIGN KEY (`project_id`,`issue_id`) REFERENCES `issues` (`project_id`, `issue_id`);

--
-- Constraints for table `projects_history`
--
ALTER TABLE `projects_history`
  ADD CONSTRAINT `project_id_fk` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
