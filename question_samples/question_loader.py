from bs4 import BeautifulSoup
import re

#
# Daten von: http://www.gamefaqs.com/gba/919785-who-wants-to-be-a-millionaire-2nd-edition/faqs/40044
#
# 1010 von 1012 FFragen werden korrekt geparset.
#
# Ausgabe sieht so aus (Trennung über neue Zeile):
# <Frage>
# <Antwort1>
# <Antwort2>
# <Antwort3>
# <Antwort4>
# <Index der richtigen Antwort (0-3)>
# 
# <Frage>
# ...


def process_question(question):
	question = ' '.join(question.split(' ')[1:]) #remove number
	question = question.replace('\n', ' ') # remove formatting \n
	question = re.sub(' +', ' ', question) # remove formating space
	return question

def process_answers(answers):
	answers = answers.split('\n')
	#print(answers)
	# first 4, all answers start with '*'
	possible_answers = [re.sub(' ?\*', '', answers[i]) for i in range(4)]

	# always Answer: <the answer>
	# get index of right answer
	right_answer_val = answers[5].replace('Answer: ', '')
	try:
		right_answer = str(possible_answers.index(right_answer_val))
	except ValueError:
		print('Could not find', right_answer_val, 'in', possible_answers)
		return None

	possible_answers.append(right_answer)
	possible_answers.append('') #for newline after answers

	return '\n'.join(possible_answers)

def write_questions(content, output='questions.txt'):
	doc = ''

	counter = 0
	q_counter = 0
	prev_question = None

	while counter < len(content):
		current = content[counter].strip()

		if current.startswith('EOF'):
			break

		if (counter % 2 == 0):
			prev_question = process_question(current) + '\n'

		else:
			answers = process_answers(current)
			if answers is not None:	
				answers += '\n'
				doc += prev_question
				doc += answers
				q_counter += 1


		counter += 1

	f_out = open(output, 'w')
	f_out.write(doc)
	print(str(q_counter), 'fragen')
	




f = open('questions.htm', 'r')
bs = BeautifulSoup(f.read(), 'html.parser')
content = str(bs.find('pre', id='faqtext'))
splitter = '-----------------------------------------------------------------------------'

content = content.split(splitter)[1:] # first is intro text
write_questions(content)